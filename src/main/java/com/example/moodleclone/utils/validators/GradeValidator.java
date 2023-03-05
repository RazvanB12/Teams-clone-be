package com.example.moodleclone.utils.validators;

import com.example.moodleclone.exceptions.GradeOutOfBoundsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GradeValidator {
    public void validateValue(int value) throws GradeOutOfBoundsException {
        if (value < 1 || value > 100)
            throw new GradeOutOfBoundsException("Grade must be 1-100");
    }
}
