package com.Khata.Khata.Repository;

import com.Khata.Khata.Entity.Gig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GigRepository extends JpaRepository <Gig, Integer>
{

}
