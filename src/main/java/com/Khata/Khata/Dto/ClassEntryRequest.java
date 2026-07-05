package com.Khata.Khata.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassEntryRequest
{
    @NotNull
    private Integer classTypeId;

    @NotNull
    private LocalDate date;

    /** Optional override; falls back to the class type's default rate. */
    private Double rate;

    private Boolean confirmed;

    private String notes;
}
