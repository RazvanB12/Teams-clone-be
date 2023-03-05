package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.SmallAssignmentDTO;
import com.example.moodleclone.dtos.responses.SmallSolutionDTO;
import com.example.moodleclone.dtos.responses.SmallUserDTO;
import com.example.moodleclone.dtos.responses.SolutionWithUserDTO;
import com.example.moodleclone.entities.Solution;
import com.example.moodleclone.entities.User;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class SolutionFactory {
    private final ModelMapper mapper;

    public Solution createNewSolution (MultipartFile file) throws IOException {
        byte[] content = file.getBytes();
        String typeName = file.getContentType();

        Solution newSolution = new Solution();

        newSolution.setContent(content);
        newSolution.setType(typeName);
        newSolution.setDate(Instant.now());
        return newSolution;
    }

    public List<GenericDTO> mapSolutionsToGenericDTO(List<Solution> solutions) {
        List<GenericDTO> solutionDTOs = new ArrayList<>();
        solutions.forEach( solution -> {
                    SolutionWithUserDTO solutionDTO = mapSolutionToGenericDTO(solution);
                    solutionDTOs.add(solutionDTO);
                }
        );
        return solutionDTOs;
    }

    private SolutionWithUserDTO mapSolutionToGenericDTO(Solution solution) {
        SmallSolutionDTO solutionDTO = mapper.map(solution, SmallSolutionDTO.class);
        User user = solution.getUser();
        SmallUserDTO userDTO = mapper.map(user, SmallUserDTO.class);
        int grade = -1;
        if(solution.getGrade() != null){
            grade = solution.getGrade().getValue();
        }
        return new SolutionWithUserDTO(solutionDTO, userDTO, grade);
    }
}
