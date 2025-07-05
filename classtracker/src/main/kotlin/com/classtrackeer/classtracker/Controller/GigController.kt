package com.classtrackeer.classtracker.Controller

import com.classtrackeer.classtracker.Model.Gig
import com.classtrackeer.classtracker.Service.GigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/gig")
class GigController (@Autowired val gigService : GigService){

    @PostMapping("/addGig")
    fun addGig(@RequestBody gig : Gig) : ResponseEntity<Gig>{
        val savedGig : Gig = gigService.addGig(gig)
        return ResponseEntity(savedGig, HttpStatus.OK)
    }
}