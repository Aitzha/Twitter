package com.example.twitter.controllers;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.example.twitter.aws.Credentials;
import com.example.twitter.models.Media;
import com.example.twitter.models.Post;
import com.example.twitter.models.Subscription;
import com.example.twitter.repositories.MediaRepository;
import com.example.twitter.repositories.PostRepository;
import com.example.twitter.repositories.SubscriptionRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MediaRepository mediaRepository;
    private final AmazonS3 s3client;

    private String bucketName = "twitter-bucket-8580";

    PostController(
            PostRepository postRepository,
            SubscriptionRepository subscriptionRepository,
            MediaRepository mediaRepository,
            Credentials credentials) {

        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.mediaRepository = mediaRepository;
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials.credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

    @GetMapping("/my_posts")
    public Iterable<Post> getMyPosts(
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeOwnerId") String fakeOwnerId) {
        return postRepository.findByOwnerId(fakeOwnerId, Sort.by(Sort.Order.asc("time")));
    }

    @GetMapping("/feed")
    public Iterable<Post> getFeed(
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeOwnerId") String fakeOwnerId) {

        List<String> ownerIds = new ArrayList<String>();
        List<Subscription> subscriptionList = subscriptionRepository.findBySubscriberId(fakeOwnerId, Sort.by(Sort.Order.asc("subscriberId")));
        for(Subscription s : subscriptionList) {
            ownerIds.add(s.userId);
        }

        return postRepository.findByOwnerIdIn(ownerIds, Sort.by(Sort.Order.asc("time")));
    }

    @PostMapping
    public String post(
            @RequestParam("text") String text,
            @RequestHeader("authorization") String jwtToken,
            @RequestParam("fakeOwnerId") String fakeOwnerId,
            @RequestParam("image") MultipartFile image) throws IOException {

        Post newPost = new Post(fakeOwnerId, text);
        postRepository.save(newPost);

        File file = new File(System.getenv("PROJECT_HOME") + "/twitter/src/main/java/com/example/twitter/common/targetFile.tmp");
        image.transferTo(file);
        s3client.putObject(bucketName, image.getOriginalFilename(), file);

        String type = "image";
        Media newMedia = new Media(newPost.id, type, image.getOriginalFilename());
        mediaRepository.save(newMedia);

        return "Saved";
    }
}
