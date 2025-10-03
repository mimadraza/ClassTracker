package com.Khata.Khata.Entity;

import com.Khata.Khata.Enums.Days;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Gigs")
public class Gig {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer userId;
    private Days[] days;
    private double payrate;
    private double timePerDay;
}
