package com.Khata.Khata.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "month_locks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "year_value", "month_value"}))
public class MonthLock
{
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "year_value", nullable = false)
    private int yearValue;

    @Column(name = "month_value", nullable = false)
    private int monthValue;

    @Column(nullable = false)
    private Instant lockedAt;
}
