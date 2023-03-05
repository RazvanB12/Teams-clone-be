package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.UserRegisterDTO;
import com.example.moodleclone.dtos.responses.MessagesDTO;
import com.example.moodleclone.dtos.responses.SmallUserDTO;
import com.example.moodleclone.dtos.responses.UserDTO;
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
public class UserFactory {
    private final PasswordEncoder bcrypt;
    private final ModelMapper mapper;

    public User createNewUser(UserRegisterDTO userDTO) {
        User newUser = mapper.map(userDTO, User.class);
        String hashedPassword = bcrypt.encode(userDTO.getPassword());
        newUser.setPassword(hashedPassword);
        newUser.setRoleName(userDTO.getRole());
        return newUser;
    }

    public List<GenericDTO> mapUsersToSmallUserDTO(List<User> users) {
        List<GenericDTO> usersDTO = new ArrayList<>();
        users.forEach( user -> {
                    SmallUserDTO smallContactDTO = mapUserToSmallUserDTO(user);
                    usersDTO.add(smallContactDTO);
                }
        );
        return usersDTO;
    }

    private SmallUserDTO mapUserToSmallUserDTO(User user) {
        return mapper.map(user, SmallUserDTO.class);
    }

    public List<GenericDTO> mapUsersToUserDTO(List<User> users) {
        List<GenericDTO> usersDTO = new ArrayList<>();
        users.forEach( user -> {
                    UserDTO userDTO = mapUserToUserDTO(user);
                    usersDTO.add(userDTO);
                }
        );
        return usersDTO;
    }

    private UserDTO mapUserToUserDTO(User user) {
        return mapper.map(user, UserDTO.class);
    }
}

