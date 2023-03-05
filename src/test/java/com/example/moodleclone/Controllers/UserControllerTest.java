package com.example.moodleclone.Controllers;

import com.example.moodleclone.controllers.UserController;
import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.UserLoginDTO;
import com.example.moodleclone.dtos.requests.UserRegisterDTO;
import com.example.moodleclone.dtos.responses.AuthResponseDTO;
import com.example.moodleclone.dtos.responses.MyGroupsDTO;
import com.example.moodleclone.dtos.responses.UserAuthResponseDTO;
import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.RoleEnum;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.exceptions.UserAuthException;
import com.example.moodleclone.exceptions.UserNotFoundException;
import com.example.moodleclone.exceptions.UserValidationException;
import com.example.moodleclone.repositories.UserRepository;
import com.example.moodleclone.utils.JwtTokenUtil;
import com.example.moodleclone.utils.StringGenerator;
import com.example.moodleclone.utils.factories.GroupsFactory;
import com.example.moodleclone.utils.factories.UserFactory;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {
    @MockBean
    private UserRepository userRepository ;
    @Autowired
    private UserController controller = mock(UserController.class);
    @Autowired
    private UserFactory userFactory = mock(UserFactory.class);
    @Autowired
    private ModelMapper mapper = mock(ModelMapper.class);
    @Autowired
    private JwtTokenUtil jwtTokenUtil = mock(JwtTokenUtil.class);
    @Autowired
    private GroupsFactory groupsFactory = mock(GroupsFactory.class);

    @Test
    void userInvalidPasswordRegister() {
        UserRegisterDTO invalidUser = new UserRegisterDTO(
                "razvan",
                "razvan",
                "razvanbumbu@yahoo.com",
                "parola",
                RoleEnum.PROFESSOR);
        assertThrows(UserValidationException.class, () -> {
            controller.registerUser(invalidUser);
        });
    }

    @Test
    void userInvalidUsernameRegister(){
        UserRegisterDTO invalidUser = new UserRegisterDTO(
                " ",
                "razvan",
                "razvanbumbu@yahoo.com",
                "Parola10@",
                RoleEnum.PROFESSOR);
        assertThrows(UserValidationException.class, () -> {
            controller.registerUser(invalidUser);
        });
    }

    @Test
    void userInvalidNameRegister(){
        UserRegisterDTO invalidUser = new UserRegisterDTO(
                "razvan",
                "",
                "razvanbumbu@yahoo.com",
                "Parola10@",
                RoleEnum.PROFESSOR);
        assertThrows(UserValidationException.class, () -> {
            controller.registerUser(invalidUser);
        });
    }

    @Test
    void userInvalidEmailRegister(){
        UserRegisterDTO invalidUser = new UserRegisterDTO(
                "razvan",
                "razvan",
                "razvanbumbuyahoo.com",
                "Parola10@",
                RoleEnum.PROFESSOR);
        assertThrows(UserValidationException.class, () -> {
            controller.registerUser(invalidUser);
        });
    }

    @Test
    void invalidPasswordLogin(){
        UserLoginDTO invalidUser = new UserLoginDTO(
                "razvan",
                "Parola"
        );
        assertThrows(UserValidationException.class, () -> {
            controller.loginUser(invalidUser);
        });
    }

    @Test
    void invalidUnregisteredLogin(){
        UserLoginDTO invalidUser = new UserLoginDTO(
                "razvan",
                "Parola10@"
        );
        when(userRepository.findByUsername("razvan")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> {
            controller.loginUser(invalidUser);
        });
    }

    @Test
    void validLogin() throws UserNotFoundException, UserValidationException {
        UserRegisterDTO validUserRegister = new UserRegisterDTO(
                "razvan",
                "razvan",
                "razvanbumbu@yahoo.com",
                "Parola10@",
                RoleEnum.PROFESSOR);

        UserLoginDTO validUserLogin = new UserLoginDTO(
                "razvan",
                "Parola10@"
        );

        User validUser = userFactory.createNewUser(validUserRegister);
        validUser.setActive(true);
        validUser.setId(UUID.randomUUID());
        when(userRepository.findByUsername("razvan")).thenReturn(Optional.of(validUser));
        String token = jwtTokenUtil.generateToken(validUser.getId());
        UserAuthResponseDTO userResponse = mapper.map(validUser, UserAuthResponseDTO.class);
        AuthResponseDTO authResponse = new AuthResponseDTO(userResponse, token);
        ResponseEntity<GenericDTO> response = controller.loginUser(validUserLogin);
        assertThat(response.getStatusCode().equals(HttpStatus.ACCEPTED));
        assertThat(response.getBody().equals(authResponse));
    }
}