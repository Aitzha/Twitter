package com.example.twitter.models;

import com.example.twitter.common.IdGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Post {
    @Id
    public String id;
    public String ownerId;
    public String content;
    public Date postedDate;
    public long likesCount;

    public Post() {}

    public Post(String id, String ownerId, String content) {
        this.id = id;
        this.ownerId = ownerId;
        this.content = content;
        this.postedDate = new Date();
        this.likesCount = 0;
    }

}
