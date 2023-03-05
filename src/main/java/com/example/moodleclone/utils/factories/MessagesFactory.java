package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.UserRegisterDTO;
import com.example.moodleclone.dtos.responses.MessagesDTO;
import com.example.moodleclone.dtos.responses.SmallUserDTO;
import com.example.moodleclone.entities.Messages;
import com.example.moodleclone.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class MessagesFactory {
    private final ModelMapper mapper;

    public List<MessagesDTO> mapMessagesToGenericDTO(List<Messages> messages, Boolean fromMe) {
        List<MessagesDTO> messagesDTO = new ArrayList<>();
        messages.forEach( message -> {
                    MessagesDTO messageDTO = mapMessageToGenericDTO(message);
                    messageDTO.setFromMe(fromMe);
                    messagesDTO.add(messageDTO);
                }
        );
        return messagesDTO;
    }

    private MessagesDTO mapMessageToGenericDTO(Messages message) {
        return mapper.map(message, MessagesDTO.class);
    }
}

