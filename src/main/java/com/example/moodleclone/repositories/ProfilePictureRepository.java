package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.ProfilePicture;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProfilePictureRepository extends CrudRepository<ProfilePicture, UUID> {
}
