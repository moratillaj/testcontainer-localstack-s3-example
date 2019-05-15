package com.example.testcontainers.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AWSConfiguration {

    @Value("${aws.credentials.accessKey}")
    private String awsAccessKey;

    @Value("${aws.credentials.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.endpoint}")
    private String awsEndpoint;

    @Bean
    public AmazonS3 getAmazonS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(getCredentialsProvider())
                .withEndpointConfiguration(getEndpointConfiguration())
                .withPathStyleAccessEnabled(true)
                .build();
    }

    private AWSCredentialsProvider getCredentialsProvider() {
        AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    private AwsClientBuilder.EndpointConfiguration getEndpointConfiguration() {
        return new AwsClientBuilder.EndpointConfiguration(awsEndpoint, awsRegion);
    }
}
