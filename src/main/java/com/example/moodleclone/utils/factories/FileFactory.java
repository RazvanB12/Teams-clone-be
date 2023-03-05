package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.FilesDTO;
import com.example.moodleclone.dtos.responses.MessagesDTO;
import com.example.moodleclone.dtos.responses.SmallFilesDTO;
import com.example.moodleclone.entities.Files;
import com.example.moodleclone.entities.Messages;
import lombok.AllArgsConstructor;
import org.apache.catalina.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class FileFactory {
    private final ModelMapper mapper;

    public Files createNewFile (MultipartFile file) throws IOException {
        byte[] content = file.getBytes();
        String typeName = file.getContentType();
        String name = file.getOriginalFilename();

        Files newFile = new Files();

        newFile.setContent(content);
        newFile.setType(typeName);
        newFile.setDate(Instant.now());
        newFile.setName(name);
        return newFile;
    }

    public List<GenericDTO> mapFilesToGenericDTO(List<Files> files) {
        List<GenericDTO> filesDTO = new ArrayList<>();
        files.forEach( file -> {
                    SmallFilesDTO fileDTO = mapFileToGenericDTO(file);
                    filesDTO.add(fileDTO);
                }
        );
        return filesDTO;
    }

    private SmallFilesDTO mapFileToGenericDTO(Files file) {
        return mapper.map(file, SmallFilesDTO.class);
    }
}
