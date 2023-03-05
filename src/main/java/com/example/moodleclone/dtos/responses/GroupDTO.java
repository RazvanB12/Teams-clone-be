package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO implements GenericDTO {
    private UUID id;
    private String name;
    private String enrollKey;
    private SmallUserDTO owner;
}
