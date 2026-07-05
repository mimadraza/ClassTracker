package com.Khata.Khata.Repository;

import com.Khata.Khata.Entity.ClassEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassEntryRepository extends JpaRepository<ClassEntry, Integer>
{
    List<ClassEntry> findByUserIdAndDateBetweenOrderByDateAscIdAsc(Integer userId, LocalDate from, LocalDate to);

    Optional<ClassEntry> findByIdAndUserId(Integer id, Integer userId);

    long countByUserIdAndClassTypeId(Integer userId, Integer classTypeId);
}
