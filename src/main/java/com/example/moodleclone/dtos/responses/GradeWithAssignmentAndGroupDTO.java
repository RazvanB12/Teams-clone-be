package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeWithAssignmentAndGroupDTO implements GenericDTO, Comparable<GradeWithAssignmentAndGroupDTO>{
    private GradeDTO grade;
    private SmallAssignmentDTO assignment;
    private SmallGroupDTO group;

    @Override
    public int compareTo(GradeWithAssignmentAndGroupDTO o) {
        return getGrade().getDate().compareTo(o.getGrade().getDate());
    }
}
