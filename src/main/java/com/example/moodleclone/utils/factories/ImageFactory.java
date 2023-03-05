package com.example.moodleclone.utils.factories;

import com.example.moodleclone.entities.ProfilePicture;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@AllArgsConstructor
public class ImageFactory {
    private final ModelMapper modelMapper;

    public ProfilePicture createNewImage(MultipartFile file) throws IOException {
        byte[] content = scale(file.getBytes(),256,256);
        String typeName = file.getContentType();
        ProfilePicture image = new ProfilePicture();
        image.setContent(content);
        image.setType("image/jpg");
        return image;
    }

    public ProfilePicture createNewProfilePicture(MultipartFile file) throws IOException {
        return modelMapper.map(createNewImage(file),ProfilePicture.class);
    }

    private byte[] scale(byte[] fileData, int width, int height) {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);
            java.awt.Image scaledImage = img.getScaledInstance(width, height,java.awt.Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(imageBuff, "jpg", buffer);
            return buffer.toByteArray();
        } catch (IOException e){
            System.out.println("ERROR in the Image scaling method");
        }
        return null;
    }
}