package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.MyGroupsDTO;
import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.entities.UserGroups;
import com.example.moodleclone.services.NotificationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class GroupsFactory {
    private final ModelMapper mapper;
    private final NotificationService notificationService;

    public List<GenericDTO> mapGroupsToGenericDTO(List<Group> groups){
        List<GenericDTO> teamGroupsDTO = new ArrayList<>();

        groups.forEach(teamGroup -> {
                MyGroupsDTO myGroupDTO = mapGroupToGenericDTO(teamGroup);
                teamGroupsDTO.add(myGroupDTO);
                }
        );
        return teamGroupsDTO;
    }

    public MyGroupsDTO mapGroupToGenericDTO (Group group){
        MyGroupsDTO myGroupDTO = mapper.map(group, MyGroupsDTO.class);
        return myGroupDTO;
    }

    public void createPostNotifications (User user, List<UserGroups> userGroups, Group group){
        for (UserGroups userGroup : userGroups){
            if (!userGroup.getUser().equals(user)){
                String message = "User " + user.getName() + " created a new post in group " + group.getName() + "!";
                notificationService.createNotification(userGroup.getUser(), message);
            }
        }
    }

    public void createAssignmentNotifications (User user, List<UserGroups> userGroups, Group group){
        for (UserGroups userGroup : userGroups){
            if (!userGroup.getUser().equals(user)){
                String message = "User " + user.getName() + " created a new assignment in group " + group.getName() + "!";
                notificationService.createNotification(userGroup.getUser(), message);
            }
        }
    }

    public void createFileNotifications (User user, List<UserGroups> userGroups, Group group){
        for (UserGroups userGroup : userGroups){
            if (!userGroup.getUser().equals(user)){
                String message = "User " + user.getName() + " uploaded a new file in group " + group.getName() + "!";
                notificationService.createNotification(userGroup.getUser(), message);
            }
        }
    }

    public void createSolutionNotification (User student, User professor){
        String message = "User " + student.getName() + " uploaded a solution for assigment!";
        notificationService.createNotification(professor, message);
    }

    public void createGradeNotification (User student){
        String message = "You got a new grade!";
        notificationService.createNotification(student, message);
    }
}
