package com.mert.service;

import com.mert.entity.Post;
import com.mert.entity.User;
import com.mert.repository.PostRepository;
import com.mert.repository.UserRepository;

import java.util.Optional;

public class PostService {
    private final PostRepository postRepository; // bellekte boşa yer kaplamaması için
    private final UserRepository userRepository;

    public PostService(){
        this.postRepository = new PostRepository();
        this.userRepository = new UserRepository();
    }

    // amaç controllerdan bilgi almak. iş yükü burada.
    public void addNewPost(Long userId,String comment,String imageUrl,String location){
        Optional<User> user = userRepository.findById(userId);
        if(user.isEmpty()) return; // eğer user yok ise işlemi bitirir.
        postRepository.save(Post.builder()
                        .comment(comment)
                        .commentcount(0)
                        .imageurl(imageUrl)
                        .likes(0)
                        .location(location)
                        .shareddate(System.currentTimeMillis())
                        .userid(userId)
                .build());

    }
}
