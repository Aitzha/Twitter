package com.example.twitter.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    @Id
    public String userId;
    public String password;

    public User() {}

    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }
}
