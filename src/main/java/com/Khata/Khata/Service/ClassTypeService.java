package com.Khata.Khata.Service;

import com.Khata.Khata.Entity.ClassType;
import com.Khata.Khata.Exception.ApiException;
import com.Khata.Khata.Repository.ClassEntryRepository;
import com.Khata.Khata.Repository.ClassTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassTypeService
{
    private final ClassTypeRepository classTypeRepository;
    private final ClassEntryRepository classEntryRepository;

    ClassTypeService(ClassTypeRepository classTypeRepository, ClassEntryRepository classEntryRepository)
    {
        this.classTypeRepository = classTypeRepository;
        this.classEntryRepository = classEntryRepository;
    }

    public List<ClassType> list(Integer userId)
    {
        return classTypeRepository.findByUserIdOrderByNameAsc(userId);
    }

    public ClassType create(Integer userId, ClassType classType)
    {
        validate(classType);
        classType.setId(null);
        classType.setUserId(userId);
        return classTypeRepository.save(classType);
    }

    public ClassType update(Integer userId, Integer id, ClassType changes)
    {
        validate(changes);
        ClassType existing = classTypeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> ApiException.notFound("class type not found"));
        existing.setName(changes.getName().trim());
        existing.setDefaultRate(changes.getDefaultRate());
        existing.setCurrency(changes.getCurrency().trim().toUpperCase());
        return classTypeRepository.save(existing);
    }

    public void delete(Integer userId, Integer id)
    {
        ClassType existing = classTypeRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> ApiException.notFound("class type not found"));
        if (classEntryRepository.countByUserIdAndClassTypeId(userId, id) > 0)
        {
            throw ApiException.conflict("this class type has logged classes; delete those entries first");
        }
        classTypeRepository.delete(existing);
    }

    private void validate(ClassType classType)
    {
        if (classType.getName() == null || classType.getName().trim().isEmpty())
        {
            throw ApiException.badRequest("name is required");
        }
        if (classType.getDefaultRate() < 0)
        {
            throw ApiException.badRequest("default rate cannot be negative");
        }
        if (classType.getCurrency() == null || classType.getCurrency().trim().isEmpty())
        {
            classType.setCurrency("USD");
        }
    }
}
