package com.example.moodleclone.dtos.requests;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.IUser;
import com.example.moodleclone.entities.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO implements GenericDTO, IUser {

    private String username;
    private String name;
    private String email;
    private String password;
    private RoleEnum role;

}
