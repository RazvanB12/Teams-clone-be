package com.example.moodleclone.services;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.SendMessageDTO;
import com.example.moodleclone.dtos.responses.GetMessagesDTO;
import com.example.moodleclone.dtos.responses.MessagesDTO;
import com.example.moodleclone.dtos.responses.SmallUserDTO;
import com.example.moodleclone.entities.Messages;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.exceptions.MessageLengthException;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.exceptions.UserNotFoundException;
import com.example.moodleclone.repositories.MessageRepository;
import com.example.moodleclone.repositories.UserGroupsRepository;
import com.example.moodleclone.repositories.UserRepository;
import com.example.moodleclone.utils.JwtTokenUtil;
import com.example.moodleclone.utils.factories.GroupsFactory;
import com.example.moodleclone.utils.factories.MessagesFactory;
import com.example.moodleclone.utils.factories.UserFactory;
import com.example.moodleclone.utils.validators.MessageValidator;
import com.example.moodleclone.utils.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class MessagesService {
    final private UserRepository userRepository;
    final private MessageRepository messageRepository;

    final private MessagesFactory messagesFactory;

    final private UserValidator userValidator;
    final private MessageValidator messageValidator;

    final private NotificationService notificationService;

    final private ModelMapper modelMapper;

    public ResponseEntity<GenericDTO> sendMessage(SendMessageDTO content, String token, UUID receiverID) throws UserAuthException, UserNotFoundException, MessageLengthException {
        User sender = userValidator.authUser(token);
        Optional<User> receiver = userRepository.findById(receiverID);
        messageValidator.validateMessageLength(content.getContent());

        if (receiver.isPresent()){
            Messages message = new Messages();
            message.setSender(sender);
            message.setReceiver(receiver.get());
            message.setContent(content.getContent());
            message.setDate(Instant.now());
            messageRepository.save(message);

            String notification = "Message from " + sender.getName() + ": " + content.getContent();
            notificationService.createNotification(receiver.get(), notification);

            return ResponseEntity.ok().body(null);
        }

        throw new UserNotFoundException("Receiver doesn't exist");
    }

    public ResponseEntity<GetMessagesDTO> getMessages(String token, UUID receiverID) throws UserAuthException, UserNotFoundException {
        User sender = userValidator.authUser(token);

        Optional<User> receiver = userRepository.findById(receiverID);
        if (receiver.isEmpty()){
            throw new UserNotFoundException("Receiver doesn't exist");
        }

        List<Messages> sent = messageRepository.findBySenderAndReceiver(sender, receiver.get());
        List<Messages> received = messageRepository.findBySenderAndReceiver(receiver.get(), sender);

        List<MessagesDTO> receivedDTO = messagesFactory.mapMessagesToGenericDTO(received, false);
        List<MessagesDTO> sentDTO = messagesFactory.mapMessagesToGenericDTO(sent, true);

        List<MessagesDTO> messagesDTO = new ArrayList<>();
        messagesDTO.addAll(receivedDTO);
        messagesDTO.addAll(sentDTO);
        Collections.sort(messagesDTO);

        GetMessagesDTO response = new GetMessagesDTO();
        response.setMessages(messagesDTO);
        response.setSender(modelMapper.map(sender, SmallUserDTO.class));
        response.setReceiver(modelMapper.map(receiver, SmallUserDTO.class));

        return ResponseEntity.ok().body(response);
    }
}
