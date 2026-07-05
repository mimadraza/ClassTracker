package com.Khata.Khata.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntryView
{
    private Integer id;
    private Integer classTypeId;
    private String classTypeName;
    private String currency;
    private LocalDate date;
    private double rate;
    private boolean confirmed;
    private String notes;
}
