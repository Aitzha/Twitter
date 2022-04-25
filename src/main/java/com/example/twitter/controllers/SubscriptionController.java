package com.example.twitter.controllers;

import com.example.twitter.models.Subscription;
import com.example.twitter.repositories.SubscriptionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/subscription")
public class SubscriptionController {
    private final SubscriptionRepository subscriptionRepository;

    SubscriptionController(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @GetMapping("/subscribers")
    public Iterable<Subscription> getSubscribers(
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeUserId") String fakeUserId) {
        return subscriptionRepository.findByUserId(fakeUserId, Sort.by(Sort.Order.asc("userId")));
    }

    @GetMapping("/subscriptions")
    public Iterable<Subscription> getSubscriptions(
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeSubscriberId") String fakeSubscriberId) {
        return subscriptionRepository.findBySubscriberId(fakeSubscriberId, Sort.by(Sort.Order.asc("subscriberId")));
    }

    @PostMapping
    public String post(
            @RequestParam("userId") String userId,
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeSubscriberId") String fakeSubscriberId) {

        Subscription newSubscription = new Subscription(fakeSubscriberId, userId);
        subscriptionRepository.save(newSubscription);
        return "Saved";
    }
}
