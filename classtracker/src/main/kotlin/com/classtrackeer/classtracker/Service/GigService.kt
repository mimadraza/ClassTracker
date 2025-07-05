package com.classtrackeer.classtracker.Service

import com.classtrackeer.classtracker.Model.Gig
import com.classtrackeer.classtracker.Repository.GigRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
class GigService (@Autowired val gigRepository : GigRepository){

    fun addGig(gig : Gig) : Gig {
        return gigRepository.save(gig)
    }
}