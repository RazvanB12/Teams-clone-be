package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.Solution;
import com.example.moodleclone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SolutionRepository extends CrudRepository<Solution, UUID> {
    Optional<Solution> findByUserAndAssignment(User user, Assignments assignments);
    List<Solution> findByAssignment(Assignments assignment);
    List<Solution> findByUser(User user);
}
