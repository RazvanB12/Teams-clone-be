package com.example.moodleclone.services;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.UpdateUserDTO;
import com.example.moodleclone.dtos.requests.UserLoginDTO;
import com.example.moodleclone.dtos.requests.UserRegisterDTO;
import com.example.moodleclone.dtos.responses.AuthResponseDTO;
import com.example.moodleclone.dtos.responses.GradeWithAssignmentAndGroupDTO;
import com.example.moodleclone.dtos.responses.SmallAssignmentDTO;
import com.example.moodleclone.dtos.responses.UserAuthResponseDTO;
import com.example.moodleclone.entities.*;
import com.example.moodleclone.exceptions.*;
import com.example.moodleclone.repositories.ProfilePictureRepository;
import com.example.moodleclone.repositories.SolutionRepository;
import com.example.moodleclone.repositories.UserGroupsRepository;
import com.example.moodleclone.repositories.UserRepository;
import com.example.moodleclone.utils.JwtTokenUtil;
import com.example.moodleclone.utils.factories.*;
import com.example.moodleclone.utils.validators.ProfilePictureValidator;
import com.example.moodleclone.utils.validators.UserValidator;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
public class UserService {
    final private UserRepository userRepository;
    final private UserGroupsRepository userGroupsRepository;
    final private ProfilePictureRepository profilePictureRepository;
    final private SolutionRepository solutionRepository;

    final private UserFactory userFactory;
    final private GroupsFactory groupsFactory;
    final private ImageFactory imageFactory;
    final private GradeFactory gradeFactory;

    final private ModelMapper modelMapper;
    final private JwtTokenUtil jwtTokenUtil;
    final private PasswordEncoder bcrypt;

    final private UserValidator userValidator;
    final private ProfilePictureValidator profilePictureValidator;

    public ResponseEntity<GenericDTO> registerUser(UserRegisterDTO userDTO) throws UserValidationException {
        userValidator.validatePassword(userDTO.getPassword());

        User newUser = userFactory.createNewUser(userDTO);
        userValidator.validateUser(userDTO, true);
        newUser.setActive(Boolean.FALSE);
        User savedUser = userRepository.save(newUser);

        UserAuthResponseDTO userResponse = modelMapper.map(savedUser, UserAuthResponseDTO.class);
        String token = jwtTokenUtil.generateToken(savedUser.getId());
        AuthResponseDTO response = new AuthResponseDTO(userResponse, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<GenericDTO> updateUser(UpdateUserDTO userDTO, String token) throws UserValidationException, UserAuthException {
        User user = userValidator.authUser(token);

        if (userDTO.getNewPassword() != null && userDTO.getNewPassword().length() > 0) {
            userValidator.validatePassword(userDTO.getNewPassword());
            if (userDTO.getOldPassword() == null || userDTO.getOldPassword().length() == 0 || !bcrypt.matches(userDTO.getOldPassword(), user.getPassword())) {
                throw new UserValidationException("Old password is incorrect!");
            }
            user.setPassword(bcrypt.encode(userDTO.getNewPassword()));
        }

        userValidator.validateUser(userDTO, true);

        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        User returnedUser = userRepository.save(user);

        UserAuthResponseDTO response = modelMapper.map(returnedUser, UserAuthResponseDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    public ResponseEntity<GenericDTO> activateUser(String token, UUID userId) throws UserAuthException, UserValidationException, UserNotFoundException {
        User admin = userValidator.authUser(token);
        userValidator.verifyIfAdmin(admin);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()){
            throw new UserNotFoundException("User doesn't exist");
        }

        User foundedUser = user.get();
        foundedUser.setActive(true);
        userRepository.save(foundedUser);

        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<GenericDTO> deactivateUser(String token, UUID userId) throws UserAuthException, UserValidationException, UserNotFoundException {
        User admin = userValidator.authUser(token);
        userValidator.verifyIfAdmin(admin);

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()){
            throw new UserNotFoundException("User doesn't exist");
        }

        User foundedUser = user.get();
        foundedUser.setActive(false);
        userRepository.save(foundedUser);

        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<GenericDTO> loginUser(UserLoginDTO userDTO) throws UserNotFoundException, UserValidationException {
        userValidator.validatePassword(userDTO.getPassword());

        Optional<User> foundedUser = userRepository.findByUsername(userDTO.getUsername());

        if (foundedUser.isEmpty()) {
            throw new UserNotFoundException("User doesn't exist");
        }

        if (!foundedUser.get().getActive()){
            throw new UserValidationException("Inactive account");
        }

        if (bcrypt.matches(userDTO.getPassword(), foundedUser.get().getPassword())) {
            String token = jwtTokenUtil.generateToken(foundedUser.get().getId());
            UserAuthResponseDTO userResponse = modelMapper.map(foundedUser.get(), UserAuthResponseDTO.class);
            AuthResponseDTO response = new AuthResponseDTO(userResponse, token);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        }

        throw new UserNotFoundException("Invalid username or password");
    }

    public ResponseEntity<List<GenericDTO>> getGroups(String token) throws UserAuthException {
        User user = userValidator.authUser(token);
        List<UserGroups> userGroups = userGroupsRepository.findByUser(user);
        List<Group> groups = new ArrayList<>();

        userGroups.forEach(userGroup -> {
            groups.add(userGroup.getGroup());
        });

        Collections.sort(groups);

        List<GenericDTO> teamGroupDTO = groupsFactory.mapGroupsToGenericDTO(groups);
        return ResponseEntity.ok().body(teamGroupDTO);
    }

    public ResponseEntity<List<GenericDTO>> getContacts(String token) throws UserAuthException {
        User user = userValidator.authUser(token);
        List<UserGroups> userGroups = userGroupsRepository.findByUser(user);

        List<User> contacts = new ArrayList<>();

        userGroups.forEach(userGroup -> {
            Group group = userGroup.getGroup();

            List<UserGroups> userGroups1 = userGroupsRepository.findByGroup(group);

            userGroups1.forEach(userGroup1 -> {
                if (!userGroup1.getUser().equals(user) && !contacts.contains(userGroup1.getUser())) {
                    contacts.add(userGroup1.getUser());
                }
            });
        });

        Collections.sort(contacts);

        List<GenericDTO> contactsDTO = userFactory.mapUsersToSmallUserDTO(contacts);

        return ResponseEntity.ok().body(contactsDTO);
    }

    public ResponseEntity<GenericDTO> saveProfilePicture(MultipartFile image, String token) throws UserAuthException, ProfilePictureException, IOException, UserNotFoundException {
        User user = userValidator.authUser(token);
        profilePictureValidator.validateImages(new MultipartFile[]{image});

        ProfilePicture newProfilePicture = imageFactory.createNewProfilePicture(image);

        ProfilePicture oldProfilePicture = user.getProfilePicture();

        if (oldProfilePicture == null) {
            newProfilePicture.setUser(user);
            user.setProfilePicture(newProfilePicture);
            User response = userRepository.save(user);
            return ResponseEntity.ok().body(modelMapper.map(response, UserAuthResponseDTO.class));
        }

        oldProfilePicture.setContent(newProfilePicture.getContent());
        oldProfilePicture.setType(newProfilePicture.getType());

        profilePictureRepository.save(oldProfilePicture);

        Optional<User> response = userRepository.findById(user.getId());

        if (response.isPresent()) {
            return ResponseEntity.ok().body(modelMapper.map(response.get(), UserAuthResponseDTO.class));
        }

        throw new UserNotFoundException("User doesn't exist");
    }

    public ResponseEntity<GenericDTO> getMyProfile(String token) throws UserAuthException {
        User user = userValidator.authUser(token);
        UserAuthResponseDTO userDTO = modelMapper.map(user, UserAuthResponseDTO.class);
        return ResponseEntity.ok().body(userDTO);
    }


    public ResponseEntity<List<GradeWithAssignmentAndGroupDTO>> getGrades(String token) throws UserAuthException {
        User user = userValidator.authUser(token);

        List<Solution> solutions = solutionRepository.findByUser(user);
        List<GradeWithAssignmentAndGroupDTO> grades = gradeFactory.mapSolutionsToGrades(solutions);
        Collections.sort(grades);
        return ResponseEntity.ok().body(grades);
    }


    public ResponseEntity<List<GenericDTO>> getAllUsers(String token) throws UserValidationException, UserAuthException {
        User admin = userValidator.authUser(token);
        userValidator.verifyIfAdmin(admin);

        List<User> users = (List<User>) userRepository.findAll();
        users.remove(admin);
        Collections.sort(users);

        List<GenericDTO> userDTOS = userFactory.mapUsersToUserDTO(users);

        return ResponseEntity.ok().body(userDTOS);
    }

    public ResponseEntity<List<GenericDTO>> getAssignments(String token) throws UserAuthException {
        User user = userValidator.authUser(token);

        List<UserGroups> userGroups = userGroupsRepository.findByUser(user);
        List<GenericDTO> result = new ArrayList<>();
        userGroups.forEach(userGroup -> {
            Group group = userGroup.getGroup();
            group.getAssignments().forEach(assignment -> {
                SmallAssignmentDTO assignmentDTO = modelMapper.map(assignment, SmallAssignmentDTO.class);
                result.add(assignmentDTO);
            });
        });

        return ResponseEntity.ok().body(result);
    }
}
