package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmallUserDTO implements GenericDTO {
    private UUID id;
    private String name;
    private String email;
    private String username;
    private ProfilePictureDTO profilePicture;
}
