package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Files;
import com.example.moodleclone.entities.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FilesRepository extends CrudRepository<Files, UUID> {
    List<Files> findByGroup (Group group);
}
