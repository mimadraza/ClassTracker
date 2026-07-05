package com.Khata.Khata.Repository;

import com.Khata.Khata.Entity.ClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassTypeRepository extends JpaRepository<ClassType, Integer>
{
    List<ClassType> findByUserIdOrderByNameAsc(Integer userId);

    Optional<ClassType> findByIdAndUserId(Integer id, Integer userId);
}
