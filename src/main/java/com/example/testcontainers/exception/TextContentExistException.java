package com.example.testcontainers.exception;

public class TextContentExistException extends RuntimeException {
    public TextContentExistException(String message) {
        super(message);
    }
}
