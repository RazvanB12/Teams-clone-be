package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.SmallAssignmentDTO;
import com.example.moodleclone.entities.Assignments;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AssignmentFactory {
    private final ModelMapper mapper;

    public List<GenericDTO> mapAssignmentsToGenericDTO(List<Assignments> assignments) {
        List<GenericDTO> assignmentsDTO = new ArrayList<>();
        assignments.forEach( assignment -> {
                    SmallAssignmentDTO assignmentDTO = mapAssignmentToGenericDTO(assignment);
                    assignmentsDTO.add(assignmentDTO);
                }
        );
        return assignmentsDTO;
    }

    private SmallAssignmentDTO mapAssignmentToGenericDTO(Assignments assignment) {
        return mapper.map(assignment, SmallAssignmentDTO.class);
    }
}
