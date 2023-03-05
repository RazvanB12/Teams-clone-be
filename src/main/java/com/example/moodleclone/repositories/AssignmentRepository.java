package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.Posts;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignments, UUID> {
    List<Assignments> findByGroup (Group group);
}
