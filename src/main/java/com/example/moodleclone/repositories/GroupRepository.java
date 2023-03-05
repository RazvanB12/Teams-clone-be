package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Group;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GroupRepository extends CrudRepository<Group, UUID> {
    Optional<Group> findByEnrollKey(String enrollKey);
}
