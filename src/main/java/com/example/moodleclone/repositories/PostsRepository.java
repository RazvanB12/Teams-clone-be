package com.example.moodleclone.repositories;

import com.example.moodleclone.entities.Group;
import com.example.moodleclone.entities.Posts;
import com.example.moodleclone.entities.UserGroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostsRepository extends CrudRepository<Posts, UUID> {
    List<Posts> findByGroup (Group group);
}
