package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Messages;
import com.example.moodleclone.entities.User;
import org.apache.logging.log4j.message.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends CrudRepository<Messages, UUID> {
    List<Messages> findBySenderAndReceiver (User sender, User receiver);
}
