package com.example.twitter.aws;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Credentials {
    public AWSCredentials credentials = new BasicAWSCredentials(
            System.getenv("AWS_ACCESS_KEY"),
            System.getenv("AWS_SECRET_KEY")
    );
}
