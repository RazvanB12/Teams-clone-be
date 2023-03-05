package com.example.moodleclone.dtos.requests;

import com.example.moodleclone.dtos.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendPostDTO implements GenericDTO {
    private String content;
}
