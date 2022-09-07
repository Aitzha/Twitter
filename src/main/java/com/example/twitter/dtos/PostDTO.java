package com.example.twitter.dtos;

import com.example.twitter.models.Post;

import java.util.Date;

public class PostDTO {
    public String ownerId;
    public String content;
    public Date postedDate;
    public long likesCount;
    public String imageURL;

    public PostDTO() {}

    public PostDTO(Post post, String imageURL) {
        this.ownerId = post.ownerId;
        this.content = post.content;
        this.postedDate = post.postedDate;
        this.likesCount = post.likesCount;
        this.imageURL = imageURL;
    }
}
