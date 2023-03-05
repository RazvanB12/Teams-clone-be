package com.example.moodleclone.dtos.requests;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.IUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO implements GenericDTO, IUser {

    private String username;
    private String name;
    private String email;
    private String newPassword;
    private String oldPassword;

}
