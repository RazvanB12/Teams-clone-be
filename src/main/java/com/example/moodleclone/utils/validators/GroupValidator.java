package com.example.moodleclone.utils.validators;

import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.User;
import com.example.moodleclone.exceptions.ConstrainsException;
import com.example.moodleclone.exceptions.GroupOwnerException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@AllArgsConstructor
public class GroupValidator {

    public void validateName(String name) throws ConstrainsException {
        if(!Pattern.matches("([\\w ]+)",name)){
            throw new ConstrainsException("Invalid group name!");
        }
    }

    public void validateAssignmentTitle(String title) throws ConstrainsException {
        if(title.length() < 3 || title.length() > 30){
            throw new ConstrainsException("Title must be between 3 and 30 characters");
        }
    }
    public void validateAssignmentDescription(String desc) throws ConstrainsException {
        if(desc.length() < 10 || desc.length() > 1000){
            throw new ConstrainsException("Description must be between 10 and 1000 characters");
        }
    }

    public void validateOwner(Group group, User user) throws GroupOwnerException {
        if (!user.equals(group.getOwner())) {
            throw new GroupOwnerException("Only group owner can create an assignment");
        }
    }
}
