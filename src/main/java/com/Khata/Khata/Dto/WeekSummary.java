package com.Khata.Khata.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekSummary
{
    private int weekNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<ClassEntryView> entries;
    private int totalCount;
    private int confirmedCount;
    private double confirmedAmount;
}
