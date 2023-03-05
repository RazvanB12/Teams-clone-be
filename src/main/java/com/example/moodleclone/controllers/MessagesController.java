package com.example.moodleclone.controllers;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.SendMessageDTO;
import com.example.moodleclone.dtos.responses.GetMessagesDTO;
import com.example.moodleclone.exceptions.MessageLengthException;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.exceptions.UserNotFoundException;
import com.example.moodleclone.services.MessagesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class MessagesController {
    private final MessagesService service;

    @PostMapping("/chat/messages/{id}")
    public ResponseEntity<GenericDTO> sendMessage(@RequestBody SendMessageDTO content, @RequestHeader String token, @PathVariable(name = "id") UUID receiverID) throws UserAuthException, UserNotFoundException, MessageLengthException {
        return service.sendMessage(content, token, receiverID);
    }

    @GetMapping("/chat/messages/{id}")
    public ResponseEntity<GetMessagesDTO> getMessages(@RequestHeader String token, @PathVariable(name = "id") UUID receiverID) throws UserAuthException, UserNotFoundException {
        return service.getMessages(token, receiverID);
    }
}
