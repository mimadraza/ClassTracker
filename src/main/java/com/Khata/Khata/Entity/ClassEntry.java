package com.Khata.Khata.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "classes")
public class ClassEntry
{
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private Integer classTypeId;

    @Column(name = "class_date", nullable = false)
    private LocalDate date;

    /** Rate snapshot taken at entry time; editable per entry. */
    @Column(nullable = false)
    private double rate;

    @Column(nullable = false)
    private boolean confirmed;

    private String notes;
}
