package com.example.moodleclone.utils.validators;

import com.example.moodleclone.exceptions.MessageLengthException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageValidator {

    public void validateMessageLength (String message) throws MessageLengthException {
        if (message.length() > 1000)
            throw new MessageLengthException("Messages, assignments and posts must have less than 1000 characters");
    }
}
