package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetMessagesDTO implements GenericDTO {
    private SmallUserDTO sender;
    private SmallUserDTO receiver;
    private List<MessagesDTO> messages;
}
