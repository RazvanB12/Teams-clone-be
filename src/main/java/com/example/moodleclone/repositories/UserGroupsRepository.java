package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.entities.UserGroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserGroupsRepository extends CrudRepository<UserGroups, UUID> {
    List<UserGroups> findByUser (User user);
    List<UserGroups> findByGroup (Group group);
    Optional<UserGroups> findByUserAndGroup (User user, Group group);
}
