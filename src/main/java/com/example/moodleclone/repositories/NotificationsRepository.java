package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.Notifications;
import com.example.moodleclone.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationsRepository extends CrudRepository<Notifications, UUID> {
    List<Notifications> findByUserAndSeen (User user, Boolean seen);
}
