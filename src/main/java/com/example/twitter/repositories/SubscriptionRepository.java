package com.example.twitter.repositories;

import com.example.twitter.models.Subscription;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends PagingAndSortingRepository<Subscription, Integer> {
    List<Subscription> findByUserId(String userId, Sort sort);

    List<Subscription> findBySubscriberId(String subscriberId, Sort sort);
}
