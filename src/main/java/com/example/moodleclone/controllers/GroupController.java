package com.example.moodleclone.controllers;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.CreateAssignmentDTO;
import com.example.moodleclone.dtos.requests.CreateGradeDTO;
import com.example.moodleclone.dtos.requests.CreateGroupDTO;
import com.example.moodleclone.dtos.requests.SendPostDTO;
import com.example.moodleclone.exceptions.*;
import com.example.moodleclone.services.GroupService;
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
public class GroupController {

    private final GroupService service;

    @PostMapping("/group/join")
    public ResponseEntity<GenericDTO> joinGroup(@RequestHeader String token, @RequestParam String enrollKey) throws  UserAuthException, GroupNotFoundException, AlreadyInGroupException {
        return service.joinGroup(token, enrollKey);
    }

    @PostMapping("/group/new-group")
    public ResponseEntity<GenericDTO> createNewGroup(@RequestBody CreateGroupDTO groupDTO, @RequestHeader String token) throws UserValidationException, ConstrainsException, UserAuthException, AlreadyInGroupException, GroupNotFoundException {
        return service.createGroup(groupDTO,token);
    }

    @PostMapping("/group/upload-file/{id}")
    public ResponseEntity<GenericDTO> uploadFile(@RequestParam(name = "file") MultipartFile file, @RequestHeader String token ,@PathVariable(name = "id") UUID groupId) throws UserAuthException, IOException, GroupNotFoundException, UserNotInGroupException, FileSizeException {
        return service.uploadFile(file, token, groupId);
    }

    @PostMapping("/group/upload-post/{id}")
    public ResponseEntity<GenericDTO> uploadPost(@RequestBody SendPostDTO postDTO, @RequestHeader String token , @PathVariable(name = "id") UUID groupId) throws UserAuthException, GroupNotFoundException, MessageLengthException, UserNotInGroupException {
        return service.uploadPost(postDTO, token, groupId);
    }

    @PostMapping("/group/create-assignment/{id}")
    public ResponseEntity<GenericDTO> createAssignment(@RequestBody CreateAssignmentDTO assignmentDTO, @RequestHeader String token, @PathVariable(name = "id") UUID groupId) throws GroupNotFoundException, MessageLengthException, UserAuthException, GroupOwnerException, UserNotInGroupException, ConstrainsException {
        return service.createAssignment(assignmentDTO,groupId, token);
    }

    @PostMapping("/group/upload-solution/{id}")
    public ResponseEntity<GenericDTO> uploadSolution(@RequestParam(name = "file") MultipartFile file, @RequestHeader String token ,@PathVariable(name = "id") UUID assignmentId) throws UserAuthException, IOException, GroupNotFoundException, UserNotInGroupException, FileSizeException, AssignmentNotFoundException, AssignmentCompletedException {
        return service.uploadSolution(file, token, assignmentId);
    }

    @PostMapping("/group/create-grade/{id}")
    public ResponseEntity<GenericDTO> createGrade(@RequestHeader String token , @PathVariable(name = "id") UUID solutionId, @RequestBody CreateGradeDTO value) throws UserAuthException, SolutionNotFoundException, GroupOwnerException, GradeOutOfBoundsException, GradeCompletedException {
        return service.createGrade(token, solutionId, value);
    }

    @GetMapping("/group/get-group/{id}")
    public ResponseEntity<GenericDTO> getGroup(@RequestHeader String token, @PathVariable(name = "id") UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        return service.getGroup(token, groupId);
    }

    @GetMapping("/group/assignments/{id}")
    public ResponseEntity<List<GenericDTO>> getAssignments(@RequestHeader String token, @PathVariable(name = "id") UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        return service.getAssignments(token, groupId);
    }

    @GetMapping("/group/files/{id}")
    public ResponseEntity<List<GenericDTO>> getFiles(@RequestHeader String token, @PathVariable(name = "id") UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        return service.getFiles(token, groupId);
    }

    @GetMapping("/group/posts/{id}")
    public ResponseEntity<List<GenericDTO>> getPosts(@RequestHeader String token, @PathVariable(name = "id") UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        return service.getPosts(token, groupId);
    }

    @GetMapping("/assignment/get-solution/{id}")
    public ResponseEntity<GenericDTO> getSolution(@RequestHeader String token, @PathVariable(name = "id") UUID assignmentId) throws UserAuthException, AssignmentNotFoundException, UserNotInGroupException {
        return service.getSolution(token, assignmentId);
    }

    @GetMapping("/assignment/get-all-solutions/{id}")
    public ResponseEntity<List<GenericDTO>> getAllSolutions(@RequestHeader String token, @PathVariable(name = "id") UUID assignmentId) throws UserAuthException, AssignmentNotFoundException, UserNotInGroupException, GroupOwnerException {
        return service.getAllSolutions(token, assignmentId);
    }
}
