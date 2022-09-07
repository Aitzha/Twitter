package com.example.twitter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Media {
    @Id
    public String id;
    public String postId;
    public String type;

    public Media() {}

    public Media(String id, String postId, String type) {
        this.id = id;
        this.postId = postId;
        this.type = type;
    }
}
