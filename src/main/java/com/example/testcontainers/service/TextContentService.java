package com.example.testcontainers.service;


import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.testcontainers.exception.TextContentExistException;
import com.example.testcontainers.model.TextContent;

import io.vavr.control.Try;

@Service
public class TextContentService {

    @Autowired
    private AmazonS3 amazonS3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public TextContent createTextContent(TextContent textContent) {
        return checkTextContentKeyNotExist()
                .andThen(saveTextContentToS3())
                .apply(textContent);
    }

    public Optional<TextContent> findTextContentByKey(String key) {
        return Try.of(() -> amazonS3Client.getObjectAsString(bucketName, key))
                .map(text -> TextContent.builder().key(key).text(text).build())
                .map(Optional::of)
                .recover(Exception.class, textContent -> Optional.empty())
                .getOrElseThrow(throwable -> new RuntimeException("Error finding S3 object with key " + key, throwable));
    }

    private Function<TextContent, TextContent> saveTextContentToS3() {
        return textContent -> Optional.of(amazonS3Client.putObject(bucketName, textContent.getKey(), textContent.getText()))
                .map(objectResult -> textContent)
                .orElseThrow(() -> new RuntimeException("Error saving textContent to s3. TextContent: " + textContent));
    }

    private Function<TextContent, TextContent> checkTextContentKeyNotExist() {
        return textContent -> Optional.of(findTextContentByKey(textContent.getKey()).isPresent())
                .filter(Boolean.FALSE::equals)
                .map(notExist -> textContent)
                .orElseThrow(() -> new TextContentExistException("key " + textContent.getKey() + " already exist"));
    }

}
