package com.example.moodleclone.services;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.entities.Notifications;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.exceptions.NotificationNotFoundException;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.repositories.NotificationsRepository;
import com.example.moodleclone.utils.factories.NotificationsFactory;
import com.example.moodleclone.utils.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class NotificationService {
    final private UserValidator userValidator;

    final private NotificationsRepository notificationsRepository;

    final private NotificationsFactory notificationsFactory;

    public void createNotification(User user, String message){
        Notifications notification = new Notifications();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setSeen(false);
        notification.setDate(Instant.now());
        notificationsRepository.save(notification);
    }

    public ResponseEntity<List<GenericDTO>> getNotifications(String token) throws UserAuthException, NotificationNotFoundException {
        User user = userValidator.authUser(token);
        List<Notifications> notifications = notificationsRepository.findByUserAndSeen(user, false);

        if (notifications.isEmpty()){
            throw new NotificationNotFoundException("You have 0 notifications");
        }

        Collections.sort(notifications);
        List<GenericDTO> response = notificationsFactory.mapNotificationsToGenericDTO(notifications);
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<GenericDTO> seeNotification(String token, UUID notificationId) throws UserAuthException, NotificationNotFoundException {
        userValidator.authUser(token);
        Optional<Notifications> foundedNotification = notificationsRepository.findById(notificationId);

        if (foundedNotification.isEmpty()){
            throw new NotificationNotFoundException("Notification not found");
        }

        Notifications notification = foundedNotification.get();
        notification.setSeen(true);
        notificationsRepository.save(notification);

        return ResponseEntity.ok().body(null);
    }
}
