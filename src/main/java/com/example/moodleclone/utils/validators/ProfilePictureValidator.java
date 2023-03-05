package com.example.moodleclone.utils.validators;

import com.example.moodleclone.exceptions.ProfilePictureException;
import com.example.moodleclone.repositories.ProfilePictureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class ProfilePictureValidator {
    private final ProfilePictureRepository profilePictureRepository;

    public void validateImages(MultipartFile[] images) throws ProfilePictureException {
        for(MultipartFile file : images){
            if(file.getContentType() == null || !file.getContentType().contains("image/")){
                throw new ProfilePictureException("File \""+file.getOriginalFilename()+"\" is not an image!");
            }
        }
    }
}
