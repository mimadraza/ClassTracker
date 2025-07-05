package com.classtrackeer.classtracker.Model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.sql.Timestamp
import java.time.Instant

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue
    val id : Int,
    val name : String,
    var email : String,
    var createdAt : Timestamp = Timestamp.from(Instant.now())
) {
}