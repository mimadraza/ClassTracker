package com.classtrackeer.classtracker.Model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "Gig")
data class Gig(
    @Id
    @GeneratedValue
    val id : Int,
    val name : String,
    val payRate : Float,
    val days : Array<Day>,
    val userId : Int
) {

}