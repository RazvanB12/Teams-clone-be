package com.example.moodleclone.utils.validators;

import com.example.moodleclone.exceptions.FileSizeException;
import com.example.moodleclone.exceptions.ProfilePictureException;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class FileValidator {
    public void validateFileSize(MultipartFile[] files) throws FileSizeException {
        long size = 0;
        for(MultipartFile file : files){
            size = size + file.getSize();
        }

        //  2 MB max size
        if (size > 2000000){
            throw new FileSizeException("File size limit exceeded");
        }
    }
}
