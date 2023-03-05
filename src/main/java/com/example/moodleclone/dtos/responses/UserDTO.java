package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements GenericDTO {
    private UUID id;
    private String name;
    private String email;
    private String username;
    private RoleEnum role;
    private Boolean active;
}
