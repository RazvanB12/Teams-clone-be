package com.example.moodleclone.dtos.responses;

import com.example.moodleclone.dtos.GenericDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmallFilesDTO implements GenericDTO {
    private UUID id;
    private String name;
    private SmallUserDTO user;
    private byte[] content;
    private String type;
    private Instant date;
}
