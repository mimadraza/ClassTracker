package com.classtrackeer.classtracker.Repository

import com.classtrackeer.classtracker.Model.Gig
import org.springframework.data.jpa.repository.JpaRepository

interface GigRepository : JpaRepository<Gig, Int> {
}