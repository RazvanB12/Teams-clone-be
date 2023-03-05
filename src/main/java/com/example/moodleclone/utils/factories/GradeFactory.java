package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.responses.*;
import com.example.moodleclone.entities.Assignments;
import com.example.moodleclone.entities.Grade;
import com.example.moodleclone.entities.Solution;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class GradeFactory {
    private final ModelMapper mapper;

    public List<GradeWithAssignmentAndGroupDTO> mapSolutionsToGrades(List<Solution> solutions) {
        List<GradeWithAssignmentAndGroupDTO> gradeDTOS = new ArrayList<>();
        solutions.forEach( solution -> {
                    if (!(solution.getGrade() == null)){
                        GradeWithAssignmentAndGroupDTO gradeDTO = mapSolutionToGrade(solution);
                        gradeDTOS.add(gradeDTO);
                    }
                }
        );
        return gradeDTOS;
    }

    private GradeWithAssignmentAndGroupDTO mapSolutionToGrade(Solution solution) {
        Assignments assignment = solution.getAssignment();
        Grade grade = solution.getGrade();
        SmallAssignmentDTO AssignmentDTO = mapper.map(assignment, SmallAssignmentDTO.class);
        GradeDTO gradeDTO = mapper.map(grade, GradeDTO.class);
        SmallGroupDTO groupDTO = mapper.map(assignment.getGroup(), SmallGroupDTO.class);
        return new GradeWithAssignmentAndGroupDTO(gradeDTO, AssignmentDTO,groupDTO);
    }
}
