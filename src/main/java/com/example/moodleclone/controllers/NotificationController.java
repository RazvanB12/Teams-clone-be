package com.example.moodleclone.controllers;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.exceptions.NotificationNotFoundException;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.services.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class NotificationController {
    private NotificationService service;

    @GetMapping("/notifications")
    public ResponseEntity<List<GenericDTO>> getNotifications(@RequestHeader String token) throws UserAuthException, NotificationNotFoundException {
        return service.getNotifications(token);
    }

    @PutMapping("/notifications/seen/{id}")
    public ResponseEntity<GenericDTO> seeNotification(@RequestHeader String token, @PathVariable(name="id") UUID notificationId) throws UserAuthException, NotificationNotFoundException {
        return service.seeNotification(token, notificationId);
    }
}
