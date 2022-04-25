package com.example.twitter.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public String subscriberId;
    public String userId;

    public Subscription() {}

    public Subscription(String subscriberId, String userId) {
        this.subscriberId = subscriberId;
        this.userId = userId;
    }
}
