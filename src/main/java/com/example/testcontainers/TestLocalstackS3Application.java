package com.example.testcontainers;

import com.amazonaws.services.s3.AmazonS3;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class TestLocalstackS3Application {

  public static void main(String[] args) {
    SpringApplication.run(TestLocalstackS3Application.class, args);
  }

  @Component
  public class S3BucketsCreator implements CommandLineRunner {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Override
    public void run(String... strings) {
      Optional.of(bucketName)
          .filter(this::bucketNotExist)
          .ifPresent(amazonS3Client::createBucket);
    }

    private boolean bucketNotExist(String bucketName) {
      return !amazonS3Client.doesBucketExistV2(bucketName);
    }
  }

}
