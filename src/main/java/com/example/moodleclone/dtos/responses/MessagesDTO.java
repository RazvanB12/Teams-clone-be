package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.entities.Messages;
import com.example.moodleclone.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagesDTO implements GenericDTO, Comparable<MessagesDTO> {
    private UUID id;
    private Boolean fromMe;
    private String content;
    private Instant date;

    @Override
    public int compareTo(MessagesDTO o) {
        return getDate().compareTo(o.getDate());
    }
}
