package com.example.moodleclone.utils.factories;

import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.SmallPostDTO;
import com.example.moodleclone.entities.Posts;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class PostsFactory {
    private final ModelMapper mapper;

    public List<GenericDTO> mapPostsToGenericDTO(List<Posts> posts) {
        List<GenericDTO> postsDTO = new ArrayList<>();
        posts.forEach( post -> {
                    SmallPostDTO postDTO = mapPostToGenericDTO(post);
                    postsDTO.add(postDTO);
                }
        );
        return postsDTO;
    }

    private SmallPostDTO mapPostToGenericDTO(Posts post) {
        return mapper.map(post, SmallPostDTO.class);
    }
}
