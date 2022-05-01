package com.example.twitter.models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String ownerId;
    public String text;
    public Date time;

    public Post() {}

    public Post(String ownerId, String text) {
        this.ownerId = ownerId;
        this.text = text;
        this.time = new Date();
    }

}
