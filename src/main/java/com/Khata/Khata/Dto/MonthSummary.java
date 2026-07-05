package com.Khata.Khata.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthSummary
{
    private int year;
    private int month;
    private boolean locked;
    private int totalClasses;
    private int confirmedClasses;
    private double totalConfirmedAmount;
    /** Currency shared by every entry this month, or null when mixed/none. */
    private String currency;
    private List<WeekSummary> weeks;
}
