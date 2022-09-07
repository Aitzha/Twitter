package com.example.twitter.credentials;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.stereotype.Component;

@Component
public class AWSCredential {
    public AWSCredentials credentials = new BasicAWSCredentials(
            System.getenv("AWS_ACCESS_KEY"),
            System.getenv("AWS_SECRET_KEY")
    );
}
