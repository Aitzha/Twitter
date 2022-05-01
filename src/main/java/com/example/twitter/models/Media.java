package com.example.twitter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    int postId;
    String type;
    String name;

    public Media(int postId, String type, String name) {
        this.postId = postId;
        this.type = type;
        this.name = name;
    }
}
