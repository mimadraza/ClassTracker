package com.Khata.Khata.Service;

import com.Khata.Khata.Entity.Gig;
import com.Khata.Khata.Repository.GigRepository;
import org.springframework.stereotype.Service;

@Service
public class GigService {

    private GigRepository gigRepository;

    GigService(GigRepository gigRepository)
    {
        this.gigRepository = gigRepository;
    }

    public Gig addGig(Gig gig)
    {
        Gig savedGig = gigRepository.save(gig);
        return savedGig;
    }
}
