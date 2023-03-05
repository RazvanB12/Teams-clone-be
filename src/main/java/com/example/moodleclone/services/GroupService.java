package com.example.moodleclone.services;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.requests.CreateAssignmentDTO;
import com.example.moodleclone.dtos.requests.CreateGradeDTO;
import com.example.moodleclone.dtos.requests.CreateGroupDTO;
import com.example.moodleclone.dtos.requests.SendPostDTO;
import com.example.moodleclone.dtos.responses.*;
import com.example.moodleclone.entities.*;
import com.example.moodleclone.exceptions.*;
import com.example.moodleclone.repositories.*;
import com.example.moodleclone.utils.StringGenerator;
import com.example.moodleclone.utils.factories.*;
import com.example.moodleclone.utils.validators.*;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupService {
    final private GroupRepository groupRepository;
    final private UserGroupsRepository userGroupsRepository;
    final private FilesRepository filesRepository;
    final private PostsRepository postsRepository;
    final private AssignmentRepository assignmentRepository;
    final private SolutionRepository solutionRepository;
    final private GradeRepository gradeRepository;

    final private FileFactory fileFactory;
    final private AssignmentFactory assignmentFactory;
    final private SolutionFactory solutionFactory;
    final private PostsFactory postsFactory;
    final private GroupsFactory groupsFactory;

    final private ModelMapper modelMapper;

    final private GroupValidator groupValidator;
    final private UserValidator userValidator;
    final private FileValidator fileSizeValidator;
    final private MessageValidator messageValidator;
    final private GradeValidator gradeValidator;

    public ResponseEntity<GenericDTO> joinGroup(String token, String enrollKey) throws UserAuthException, GroupNotFoundException, AlreadyInGroupException{
        User user = userValidator.authUser(token);

        Optional<Group> foundedTeamGroup = groupRepository.findByEnrollKey(enrollKey);

        if (foundedTeamGroup.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exists");
        }

        Optional<UserGroups> group = userGroupsRepository.findByUserAndGroup(user, foundedTeamGroup.get());
        if (group.isPresent()){
            throw new AlreadyInGroupException("User is already in this group");
        }

        UserGroups userGroup = new UserGroups();
        userGroup.setUser(user);
        userGroup.setGroup(foundedTeamGroup.get());

        userGroupsRepository.save(userGroup);
        return ResponseEntity.ok().body(null);
    }

    public ResponseEntity<GenericDTO> createGroup(CreateGroupDTO groupDTO, String token) throws UserValidationException, UserAuthException, ConstrainsException, AlreadyInGroupException, GroupNotFoundException {
        User user = userValidator.authUser(token);
        userValidator.verifyIfProfessor(user);
        groupValidator.validateName(groupDTO.getName());

        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setOwner(user);
        group.setEnrollKey(StringGenerator.generateRandomString(10));

        Group result =  groupRepository.save(group);

        UserGroups userGroup = new UserGroups();
        userGroup.setGroup(group);
        userGroup.setUser(user);

        userGroupsRepository.save(userGroup);

        return ResponseEntity.ok().body(modelMapper.map(result, GroupDTO.class));
    }

    public ResponseEntity<GenericDTO> uploadFile(MultipartFile file, String token, UUID groupId) throws UserAuthException, IOException, GroupNotFoundException, UserNotInGroupException, FileSizeException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);
        Files newFile = fileFactory.createNewFile(file);
        fileSizeValidator.validateFileSize(new MultipartFile[]{file});

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());
        if (userGroups.isPresent()){
            newFile.setUser(user);
            newFile.setGroup(group.get());
            Files response = filesRepository.save(newFile);

            List<UserGroups> usersGroups = userGroupsRepository.findByGroup(group.get());
            groupsFactory.createFileNotifications(user, usersGroups, group.get());

            return ResponseEntity.ok().body(modelMapper.map(response, FilesDTO.class));
        }

        throw new UserNotInGroupException("User is not in this group");
    }


    public ResponseEntity<GenericDTO> uploadPost(SendPostDTO postDTO, String token, UUID groupId) throws UserAuthException, GroupNotFoundException, MessageLengthException, UserNotInGroupException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);

        messageValidator.validateMessageLength(postDTO.getContent());

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());
        if (userGroups.isPresent()){
            Posts post = new Posts();
            post.setGroup(group.get());
            post.setUser(user);
            post.setContent(postDTO.getContent());
            post.setDate(Instant.now());
            postsRepository.save(post);

            List<UserGroups> usersGroups = userGroupsRepository.findByGroup(group.get());
            groupsFactory.createPostNotifications(user, usersGroups, group.get());

            return ResponseEntity.ok().body(modelMapper.map(post, PostDTO.class));
        }

        throw new UserNotInGroupException("User is not in this group");
    }

    public ResponseEntity<GenericDTO> createAssignment(CreateAssignmentDTO assignmentDTO, UUID groupId, String token) throws GroupNotFoundException, MessageLengthException, UserAuthException, GroupOwnerException, UserNotInGroupException, ConstrainsException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);
        groupValidator.validateAssignmentDescription(assignmentDTO.getDescription());
        groupValidator.validateAssignmentTitle(assignmentDTO.getTitle());

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        groupValidator.validateOwner(group.get(), user);

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());
        if (userGroups.isPresent()){
            Assignments assignment = new Assignments();
            assignment.setGroup(group.get());
            assignment.setDescription(assignmentDTO.getDescription());
            assignment.setTitle(assignmentDTO.getTitle());
            assignment.setDate(Instant.now());
            Assignments response = assignmentRepository.save(assignment);

            List<UserGroups> usersGroups = userGroupsRepository.findByGroup(group.get());
            groupsFactory.createAssignmentNotifications(user, usersGroups, group.get());

            return ResponseEntity.ok().body(modelMapper.map(response, AssignmentDTO.class));
        }

        throw new UserNotInGroupException("User is not in this group");
    }


    public ResponseEntity<GenericDTO> uploadSolution(MultipartFile file, String token, UUID assignmentId) throws IOException, UserAuthException, FileSizeException, UserNotInGroupException, GroupNotFoundException, AssignmentNotFoundException, AssignmentCompletedException {
        User user = userValidator.authUser(token);
        Optional<Assignments> assignment = assignmentRepository.findById(assignmentId);

        Solution solution = solutionFactory.createNewSolution(file);
        fileSizeValidator.validateFileSize(new MultipartFile[]{file});

        if (assignment.isEmpty()){
            throw new AssignmentNotFoundException("Assignment doesn't exist");
        }

        Optional<Group> group = groupRepository.findById(assignment.get().getGroup().getId());

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        Optional<Solution> foundedSolution = solutionRepository.findByUserAndAssignment(user, assignment.get());

        if (foundedSolution.isPresent()){
            throw new AssignmentCompletedException("Assignment already completed");
        }

        solution.setUser(user);
        solution.setAssignment(assignment.get());
        Solution response = solutionRepository.save(solution);

        groupsFactory.createSolutionNotification(user, assignment.get().getGroup().getOwner());

        return ResponseEntity.ok().body(modelMapper.map(response, SolutionDTO.class));
    }

    public ResponseEntity<GenericDTO> createGrade(String token, UUID solutionId, CreateGradeDTO value) throws UserAuthException, SolutionNotFoundException, GroupOwnerException, GradeOutOfBoundsException, GradeCompletedException {
        User user = userValidator.authUser(token);
        Optional<Solution> solution = solutionRepository.findById(solutionId);
        gradeValidator.validateValue(value.getValue());

        if (solution.isEmpty()){
            throw new SolutionNotFoundException("Solution doesn't exist");
        }

        User owner = solution.get().getAssignment().getGroup().getOwner();

        if (!owner.equals(user)){
            throw new GroupOwnerException("Only group owner can create grades");
        }

        Optional<Grade> foundedGrade = gradeRepository.findBySolution(solution.get());

        if (foundedGrade.isPresent()){
            throw new GradeCompletedException("Grade already inserted for this solution");
        }

        Grade grade = new Grade();
        grade.setDate(Instant.now());
        grade.setValue(value.getValue());
        grade.setSolution(solution.get());

        Grade response = gradeRepository.save(grade);

        groupsFactory.createGradeNotification(solution.get().getUser());

        return ResponseEntity.ok().body(modelMapper.map(response, GradeDTO.class));
    }

    public ResponseEntity<GenericDTO> getGroup(String token, UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        GroupDTO groupDTO = modelMapper.map(group, GroupDTO.class);

        return ResponseEntity.ok().body(modelMapper.map(groupDTO, GroupDTO.class));
    }


    public ResponseEntity<List<GenericDTO>> getAssignments(String token, UUID groupId) throws UserNotInGroupException, GroupNotFoundException, UserAuthException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        List<Assignments> assignments = assignmentRepository.findByGroup(group.get());
        Collections.sort(assignments);
        List<GenericDTO> assignmentDTOS = assignmentFactory.mapAssignmentsToGenericDTO(assignments);

        return ResponseEntity.ok().body(assignmentDTOS);
    }

    public ResponseEntity<List<GenericDTO>> getFiles(String token, UUID groupId) throws GroupNotFoundException, UserAuthException, UserNotInGroupException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        List<Files> files = filesRepository.findByGroup(group.get());
        Collections.sort(files);
        List<GenericDTO> fileDTOS= fileFactory.mapFilesToGenericDTO(files);

        return ResponseEntity.ok().body(fileDTOS);
    }

    public ResponseEntity<List<GenericDTO>> getPosts(String token, UUID groupId) throws UserAuthException, GroupNotFoundException, UserNotInGroupException {
        User user = userValidator.authUser(token);
        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isEmpty()){
            throw new GroupNotFoundException("Group doesn't exist");
        }

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group.get());

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        List<Posts> posts = postsRepository.findByGroup(group.get());
        Collections.sort(posts);
        List<GenericDTO> fileDTOS= postsFactory.mapPostsToGenericDTO(posts);

        return ResponseEntity.ok().body(fileDTOS);
    }

    public ResponseEntity<GenericDTO> getSolution(String token, UUID assignmentId) throws UserAuthException, AssignmentNotFoundException, UserNotInGroupException {
        User user = userValidator.authUser(token);
        Optional<Assignments> assignment = assignmentRepository.findById(assignmentId);

        if (assignment.isEmpty()){
            throw new AssignmentNotFoundException("Assignment doesn't exist");
        }

        Group group = assignment.get().getGroup();

        Optional<UserGroups> userGroups = userGroupsRepository.findByUserAndGroup(user, group);

        if (userGroups.isEmpty()){
            throw new UserNotInGroupException("User is not in this group");
        }

        Optional<Solution> solution = solutionRepository.findByUserAndAssignment(user, assignment.get());

        if(solution.isEmpty()){
            return ResponseEntity.ok().body(new SmallSolutionDTO());
        }

        return ResponseEntity.ok().body(modelMapper.map(solution.get(), SmallSolutionDTO.class));
    }

    public ResponseEntity<List<GenericDTO>> getAllSolutions(String token, UUID assignmentId) throws AssignmentNotFoundException, UserAuthException, UserNotInGroupException, GroupOwnerException {
        User user = userValidator.authUser(token);
        Optional<Assignments> assignment = assignmentRepository.findById(assignmentId);

        if (assignment.isEmpty()){
            throw new AssignmentNotFoundException("Assignment doesn't exist");
        }

        User owner = assignment.get().getGroup().getOwner();

        if (!user.equals(owner)){
            throw new GroupOwnerException("Only the owner of the group can see all the solutions");
        }

        List<Solution> solutions = solutionRepository.findByAssignment(assignment.get());
        Collections.sort(solutions);
        List<GenericDTO> solutionDTOS= solutionFactory.mapSolutionsToGenericDTO(solutions);

        return ResponseEntity.ok().body(solutionDTOS);
    }
}
