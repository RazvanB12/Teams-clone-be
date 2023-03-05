package com.example.moodleclone.controllers;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.*;
import com.example.moodleclone.dtos.responses.GradeWithAssignmentAndGroupDTO;
import com.example.moodleclone.exceptions.*;
import com.example.moodleclone.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService service;

    @PostMapping("/user/register")
    public ResponseEntity<GenericDTO> registerUser(@RequestBody UserRegisterDTO user) throws UserValidationException {
        return service.registerUser(user);
    }

    @PostMapping("/user/login")
    public ResponseEntity<GenericDTO> loginUser(@RequestBody UserLoginDTO user) throws UserNotFoundException, UserValidationException {
        return service.loginUser(user);
    }

    @GetMapping("/user/groups")
    public ResponseEntity<List<GenericDTO>> getGroups(@RequestHeader String token) throws UserAuthException {
        return service.getGroups(token);
    }

    @GetMapping("/user/profile")
    public ResponseEntity<GenericDTO> getMyProfile(@RequestHeader String token) throws UserAuthException {
        return service.getMyProfile(token);
    }

    @PutMapping("/user/profile")
    public ResponseEntity<GenericDTO> getMyProfile(@RequestBody UpdateUserDTO userData,@RequestHeader String token) throws UserAuthException, UserValidationException {
        return service.updateUser(userData,token);
    }

    @GetMapping("/chat/contacts")
    public ResponseEntity<List<GenericDTO>> getContacts(@RequestHeader String token) throws UserAuthException {
        return service.getContacts(token);
    }

    @PostMapping("/user/profile-picture/save")
    public ResponseEntity<GenericDTO> saveProfilePicture(@RequestParam(name = "image") MultipartFile file, @RequestHeader String token) throws UserAuthException, ProfilePictureException, IOException, UserNotFoundException {
        return service.saveProfilePicture(file, token);
    }

    @GetMapping("/user/grades")
    public ResponseEntity<List<GradeWithAssignmentAndGroupDTO>> getGrades(@RequestHeader String token) throws UserAuthException {
        return service.getGrades(token);
    }

    @GetMapping("/user/assignments")
    public ResponseEntity<List<GenericDTO>> getAssignments(@RequestHeader String token) throws UserAuthException {
        return service.getAssignments(token);
    }

    @PutMapping("/admin/activate/{id}")
    public ResponseEntity<GenericDTO> activateUser(@RequestHeader String token, @PathVariable(name = "id") UUID userId ) throws UserValidationException, UserAuthException, UserNotFoundException {
        return service.activateUser(token, userId);
    }

    @PutMapping("/admin/deactivate/{id}")
    public ResponseEntity<GenericDTO> deactivateUser(@RequestHeader String token, @PathVariable(name = "id") UUID userId ) throws UserValidationException, UserAuthException, UserNotFoundException {
        return service.deactivateUser(token, userId);
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<List<GenericDTO>> getAllUsers(@RequestHeader String token) throws UserValidationException, UserAuthException {
        return service.getAllUsers(token);
    }
}
