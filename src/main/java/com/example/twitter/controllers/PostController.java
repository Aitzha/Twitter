package com.example.twitter.controllers;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.twitter.common.IdGenerator;
import com.example.twitter.credentials.AWSCredential;
import com.example.twitter.dtos.PostDTO;
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
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    AWSCredential awsCredential;
    private final PostRepository postRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MediaRepository mediaRepository;
    private final AmazonS3 s3client;

    private String bucketName = "twitter-bucket-2708";
    Regions clientRegion = Regions.EU_CENTRAL_1;

    PostController(
            PostRepository postRepository,
            SubscriptionRepository subscriptionRepository,
            MediaRepository mediaRepository,
            AWSCredential credentials) {

        this.postRepository = postRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.mediaRepository = mediaRepository;
        this.s3client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials.credentials))
                .withRegion(Regions.EU_CENTRAL_1)
                .build();
    }

//    @GetMapping("/my_posts")
//    public Iterable<Post> getMyPosts(
//            @RequestHeader("authorization") String jwtToken,
//            @RequestParam("fakeOwnerId") String fakeOwnerId) {
//        return postRepository.findByOwnerId(fakeOwnerId, Sort.by(Sort.Order.asc("time")));
//    }
//
//    @GetMapping("/feed")
//    public Iterable<Post> getFeed(
//            @RequestHeader("authorization") String jwtToken,
//            @RequestParam("fakeOwnerId") String fakeOwnerId) {
//
//        List<String> ownerIds = new ArrayList<String>();
//        List<Subscription> subscriptionList = subscriptionRepository.findBySubscriberId(fakeOwnerId, Sort.by(Sort.Order.asc("subscriberId")));
//        for(Subscription s : subscriptionList) {
//            ownerIds.add(s.userId);
//        }
//
//        return postRepository.findByOwnerIdIn(ownerIds, Sort.by(Sort.Order.asc("time")));
//    }


    //Had request header @RequestHeader("authorization") String jwtToken
    @CrossOrigin(origins = "http://localhost:4200/")
    @PostMapping
    public String post(
            @RequestParam("text") String text,
            @RequestParam("fakeOwnerId") String fakeOwnerId,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException, Exception {

        //create new post and save it
        //find available id for new post
        String newPostId;
        while(true) {
            newPostId = IdGenerator.generateId(10);
            if(postRepository.findById(newPostId).isEmpty()) {
                break;
            }
        }
        Post newPost = new Post(newPostId, fakeOwnerId, text);
        postRepository.save(newPost);


        //save image in AWS S3
        if(image != null && !image.isEmpty()) {
            //find available id for given image
            String newImageId;
            while(true) {
                newImageId = IdGenerator.generateId(50);
                if(!s3client.doesObjectExist(bucketName, newImageId)) {
                    break;
                }
            }
            File file = new File(System.getenv("PROJECT_HOME") + "/src/main/java/com/example/twitter/common/targetFile.tmp");
            image.transferTo(file);
            s3client.putObject(bucketName, newImageId, file);

            //save post's id image belongs to
            String type = image.getContentType();
            Media newMedia = new Media(newImageId, newPostId, type);
            mediaRepository.save(newMedia);
        }

        return "Saved";
    }

//    @CrossOrigin(origins = "https://rocky-lowlands-32511.herokuapp.com/")
    @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping
    public Iterable<PostDTO> getPosts() {

        //find all posts and create new postDTOs array
        Iterable<Post> posts = postRepository.findAll();
        List<PostDTO> postDTOS = new ArrayList<>();

        //Iterate over all posts
        for(Post post : posts) {

            Media media = mediaRepository.findByPostId(post.id);
            if(media == null) {
                postDTOS.add(new PostDTO(post, "no image found"));
                continue;
            }

            String objectKey = media.id;
            try {
                // Set the presigned URL to expire after one hour.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = Instant.now().toEpochMilli();
                expTimeMillis += 1000 * 60;

                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                System.out.println("Generating pre-signed URL.");
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, objectKey)
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

                System.out.println("Pre-Signed URL: " + url.toString());

                PostDTO newPostDTO = new PostDTO(post, url.toString());
                postDTOS.add(newPostDTO);

            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }
        }
        return postDTOS;
    }
}
