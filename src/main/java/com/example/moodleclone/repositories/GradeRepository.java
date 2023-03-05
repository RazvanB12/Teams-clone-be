package com.example.moodleclone.repositories;


import com.example.moodleclone.entities.Grade;
import com.example.moodleclone.entities.Solution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GradeRepository extends CrudRepository<Grade, UUID> {
    Optional<Grade> findBySolution(Solution solution);
}
