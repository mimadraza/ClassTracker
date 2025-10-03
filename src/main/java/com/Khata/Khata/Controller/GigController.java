package com.Khata.Khata.Controller;

import com.Khata.Khata.Entity.Gig;
import com.Khata.Khata.Service.GigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Gig")
public class GigController
{
    private GigService gigService;

    GigController(GigService gigService)
    {
        this.gigService = gigService;
    }

    @PostMapping("/add")
    public ResponseEntity<Gig> addGig(@RequestBody Gig gig)
    {
        Gig savedGig = gigService.addGig(gig);
        return ResponseEntity.ok(savedGig);
    }
}
