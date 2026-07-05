package com.Khata.Khata.Service;

import com.Khata.Khata.Dto.ClassEntryRequest;
import com.Khata.Khata.Entity.ClassEntry;
import com.Khata.Khata.Entity.ClassType;
import com.Khata.Khata.Exception.ApiException;
import com.Khata.Khata.Repository.ClassEntryRepository;
import com.Khata.Khata.Repository.ClassTypeRepository;
import com.Khata.Khata.Repository.MonthLockRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClassEntryService
{
    private final ClassEntryRepository classEntryRepository;
    private final ClassTypeRepository classTypeRepository;
    private final MonthLockRepository monthLockRepository;

    ClassEntryService(ClassEntryRepository classEntryRepository,
                      ClassTypeRepository classTypeRepository,
                      MonthLockRepository monthLockRepository)
    {
        this.classEntryRepository = classEntryRepository;
        this.classTypeRepository = classTypeRepository;
        this.monthLockRepository = monthLockRepository;
    }

    public ClassEntry create(Integer userId, ClassEntryRequest request)
    {
        ClassType classType = requireClassType(userId, request.getClassTypeId());
        assertMonthNotLocked(userId, request.getDate());

        ClassEntry entry = new ClassEntry();
        entry.setUserId(userId);
        entry.setClassTypeId(classType.getId());
        entry.setDate(request.getDate());
        entry.setRate(resolveRate(request.getRate(), classType));
        entry.setConfirmed(Boolean.TRUE.equals(request.getConfirmed()));
        entry.setNotes(request.getNotes());
        return classEntryRepository.save(entry);
    }

    public ClassEntry update(Integer userId, Integer id, ClassEntryRequest request)
    {
        ClassEntry entry = requireEntry(userId, id);
        ClassType classType = requireClassType(userId, request.getClassTypeId());
        assertMonthNotLocked(userId, entry.getDate());
        assertMonthNotLocked(userId, request.getDate());

        entry.setClassTypeId(classType.getId());
        entry.setDate(request.getDate());
        entry.setRate(resolveRate(request.getRate(), classType));
        if (request.getConfirmed() != null)
        {
            entry.setConfirmed(request.getConfirmed());
        }
        entry.setNotes(request.getNotes());
        return classEntryRepository.save(entry);
    }

    public ClassEntry setConfirmed(Integer userId, Integer id, boolean confirmed)
    {
        ClassEntry entry = requireEntry(userId, id);
        assertMonthNotLocked(userId, entry.getDate());
        entry.setConfirmed(confirmed);
        return classEntryRepository.save(entry);
    }

    public void delete(Integer userId, Integer id)
    {
        ClassEntry entry = requireEntry(userId, id);
        assertMonthNotLocked(userId, entry.getDate());
        classEntryRepository.delete(entry);
    }

    private double resolveRate(Double override, ClassType classType)
    {
        double rate = override != null ? override : classType.getDefaultRate();
        if (rate < 0)
        {
            throw ApiException.badRequest("rate cannot be negative");
        }
        return rate;
    }

    private ClassEntry requireEntry(Integer userId, Integer id)
    {
        return classEntryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> ApiException.notFound("class entry not found"));
    }

    private ClassType requireClassType(Integer userId, Integer classTypeId)
    {
        return classTypeRepository.findByIdAndUserId(classTypeId, userId)
                .orElseThrow(() -> ApiException.notFound("class type not found"));
    }

    private void assertMonthNotLocked(Integer userId, LocalDate date)
    {
        if (monthLockRepository.existsByUserIdAndYearValueAndMonthValue(userId, date.getYear(), date.getMonthValue()))
        {
            throw ApiException.conflict(date.getMonth() + " " + date.getYear() + " is locked; unlock it to make changes");
        }
    }
}
