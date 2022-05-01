package com.example.twitter.repositories;

import com.example.twitter.models.Post;
import com.example.twitter.models.Subscription;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    List<Post> findByOwnerId(String ownerId, Sort sort);
    List<Post> findByOwnerIdIn(List<String> ownerIds, Sort sort);
}
