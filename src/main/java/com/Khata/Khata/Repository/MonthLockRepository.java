package com.Khata.Khata.Repository;

import com.Khata.Khata.Entity.MonthLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthLockRepository extends JpaRepository<MonthLock, Integer>
{
    Optional<MonthLock> findByUserIdAndYearValueAndMonthValue(Integer userId, int yearValue, int monthValue);

    boolean existsByUserIdAndYearValueAndMonthValue(Integer userId, int yearValue, int monthValue);
}
