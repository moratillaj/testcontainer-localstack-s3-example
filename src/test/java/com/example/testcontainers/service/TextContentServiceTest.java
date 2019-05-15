package com.example.testcontainers.service;

import java.util.Optional;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.localstack.LocalStackContainer;

import com.example.testcontainers.TestLocalstackS3Application;
import com.example.testcontainers.model.TextContent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;


@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = TextContentServiceTest.Initializer.class, classes = TestLocalstackS3Application.class)
public class TextContentServiceTest {

    private static final String KEY = "onekey";
    private static final String TEXT = "Hello everyone!!";

    @Autowired
    private TextContentService textContentService;

    @ClassRule
    public static LocalStackContainer localStackContainer = new LocalStackContainer().withServices(S3);


    @Test
    public void shouldCreateTextContent() {
        //Given
        TextContent textContent = TextContent.builder().key(KEY).text(TEXT).build();

        //When
        TextContent created = textContentService.createTextContent(textContent);

        //Then
        assertThat(created).isNotNull();
        assertThat(created).extracting("key", "text").contains(textContent.getKey(), textContent.getText());

        Optional<TextContent> found = textContentService.findTextContentByKey(textContent.getKey());
        assertThat(found).isPresent();
        assertThat(found.get()).extracting("key", "text").contains(textContent.getKey(), textContent.getText());
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "aws.credentials.accessKey=" + localStackContainer.getDefaultCredentialsProvider().getCredentials().getAWSAccessKeyId(),
                    "aws.credentials.secretKey=" + localStackContainer.getDefaultCredentialsProvider().getCredentials().getAWSSecretKey(),
                    "aws.region=us-east-1",
                    "aws.endpoint=http://" + localStackContainer.getContainerIpAddress() + ":" + localStackContainer.getMappedPort(4572),
                    "aws.s3.bucketName=onebucket"
            );

            values.applyTo(configurableApplicationContext);
        }
    }
}