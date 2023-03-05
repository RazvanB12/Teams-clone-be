package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponseDTO implements GenericDTO {
    private UUID id;
    private String username;
    private String name;
    private String email;
    private RoleEnum role;
    private Boolean active;
    private ProfilePictureDTO profilePicture;
}
