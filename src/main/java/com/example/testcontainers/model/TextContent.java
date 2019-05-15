package com.example.testcontainers.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TextContent {
    private String key;
    private String text;
}
