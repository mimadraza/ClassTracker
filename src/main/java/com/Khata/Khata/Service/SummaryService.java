package com.Khata.Khata.Service;

import com.Khata.Khata.Dto.ClassEntryView;
import com.Khata.Khata.Dto.MonthSummary;
import com.Khata.Khata.Dto.WeekSummary;
import com.Khata.Khata.Entity.ClassEntry;
import com.Khata.Khata.Entity.ClassType;
import com.Khata.Khata.Entity.MonthLock;
import com.Khata.Khata.Exception.ApiException;
import com.Khata.Khata.Repository.ClassEntryRepository;
import com.Khata.Khata.Repository.ClassTypeRepository;
import com.Khata.Khata.Repository.MonthLockRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SummaryService
{
    private final ClassEntryRepository classEntryRepository;
    private final ClassTypeRepository classTypeRepository;
    private final MonthLockRepository monthLockRepository;

    SummaryService(ClassEntryRepository classEntryRepository,
                   ClassTypeRepository classTypeRepository,
                   MonthLockRepository monthLockRepository)
    {
        this.classEntryRepository = classEntryRepository;
        this.classTypeRepository = classTypeRepository;
        this.monthLockRepository = monthLockRepository;
    }

    public MonthSummary getSummary(Integer userId, int year, int month)
    {
        if (month < 1 || month > 12)
        {
            throw ApiException.badRequest("month must be between 1 and 12");
        }
        LocalDate first = LocalDate.of(year, month, 1);
        LocalDate last = first.with(TemporalAdjusters.lastDayOfMonth());

        List<ClassEntry> entries = classEntryRepository
                .findByUserIdAndDateBetweenOrderByDateAscIdAsc(userId, first, last);
        Map<Integer, ClassType> types = classTypeRepository.findByUserIdOrderByNameAsc(userId).stream()
                .collect(Collectors.toMap(ClassType::getId, Function.identity()));

        // Calendar weeks (Mon-Sun) clipped to the month.
        List<WeekSummary> weeks = new ArrayList<>();
        LocalDate cursor = first;
        int weekNumber = 1;
        while (!cursor.isAfter(last))
        {
            LocalDate weekEnd = cursor.with(java.time.DayOfWeek.SUNDAY);
            if (weekEnd.isAfter(last))
            {
                weekEnd = last;
            }
            final LocalDate start = cursor;
            final LocalDate end = weekEnd;
            List<ClassEntryView> weekEntries = entries.stream()
                    .filter(e -> !e.getDate().isBefore(start) && !e.getDate().isAfter(end))
                    .map(e -> toView(e, types.get(e.getClassTypeId())))
                    .collect(Collectors.toList());
            int confirmedCount = (int) weekEntries.stream().filter(ClassEntryView::isConfirmed).count();
            double confirmedAmount = weekEntries.stream()
                    .filter(ClassEntryView::isConfirmed)
                    .mapToDouble(ClassEntryView::getRate)
                    .sum();
            weeks.add(new WeekSummary(weekNumber, start, end, weekEntries,
                    weekEntries.size(), confirmedCount, confirmedAmount));
            cursor = weekEnd.plusDays(1);
            weekNumber++;
        }

        int confirmedClasses = (int) entries.stream().filter(ClassEntry::isConfirmed).count();
        double totalConfirmedAmount = entries.stream()
                .filter(ClassEntry::isConfirmed)
                .mapToDouble(ClassEntry::getRate)
                .sum();
        List<String> currencies = entries.stream()
                .map(e -> types.containsKey(e.getClassTypeId()) ? types.get(e.getClassTypeId()).getCurrency() : null)
                .filter(c -> c != null)
                .distinct()
                .collect(Collectors.toList());
        String currency = currencies.size() == 1 ? currencies.get(0) : null;
        boolean locked = monthLockRepository.existsByUserIdAndYearValueAndMonthValue(userId, year, month);

        return new MonthSummary(year, month, locked, entries.size(), confirmedClasses,
                totalConfirmedAmount, currency, weeks);
    }

    public MonthSummary lock(Integer userId, int year, int month)
    {
        if (!monthLockRepository.existsByUserIdAndYearValueAndMonthValue(userId, year, month))
        {
            monthLockRepository.save(new MonthLock(null, userId, year, month, Instant.now()));
        }
        return getSummary(userId, year, month);
    }

    public MonthSummary unlock(Integer userId, int year, int month)
    {
        monthLockRepository.findByUserIdAndYearValueAndMonthValue(userId, year, month)
                .ifPresent(monthLockRepository::delete);
        return getSummary(userId, year, month);
    }

    public String buildCsv(MonthSummary summary)
    {
        StringBuilder csv = new StringBuilder();
        csv.append("Week,Date,Class Type,Rate,Currency,Confirmed,Notes\n");
        for (WeekSummary week : summary.getWeeks())
        {
            for (ClassEntryView entry : week.getEntries())
            {
                csv.append(week.getWeekNumber()).append(',')
                        .append(entry.getDate()).append(',')
                        .append(escapeCsv(entry.getClassTypeName())).append(',')
                        .append(entry.getRate()).append(',')
                        .append(escapeCsv(entry.getCurrency())).append(',')
                        .append(entry.isConfirmed() ? "yes" : "no").append(',')
                        .append(escapeCsv(entry.getNotes())).append('\n');
            }
            csv.append("Week ").append(week.getWeekNumber()).append(" subtotal,,,")
                    .append(week.getConfirmedAmount()).append(",,")
                    .append(week.getConfirmedCount()).append(" confirmed,\n");
        }
        csv.append("GRAND TOTAL,,,")
                .append(summary.getTotalConfirmedAmount()).append(",")
                .append(summary.getCurrency() == null ? "" : summary.getCurrency()).append(",")
                .append(summary.getConfirmedClasses()).append(" confirmed,\n");
        return csv.toString();
    }

    private ClassEntryView toView(ClassEntry entry, ClassType type)
    {
        return new ClassEntryView(
                entry.getId(),
                entry.getClassTypeId(),
                type != null ? type.getName() : "(deleted)",
                type != null ? type.getCurrency() : "",
                entry.getDate(),
                entry.getRate(),
                entry.isConfirmed(),
                entry.getNotes());
    }

    private String escapeCsv(String value)
    {
        if (value == null)
        {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n"))
        {
            return '"' + value.replace("\"", "\"\"") + '"';
        }
        return value;
    }
}
