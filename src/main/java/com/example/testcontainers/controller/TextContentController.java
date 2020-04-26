package com.example.testcontainers.controller;

import com.example.testcontainers.exception.TextContentExistException;
import com.example.testcontainers.model.TextContent;
import com.example.testcontainers.service.TextContentService;
import io.vavr.control.Try;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/textContent")
public class TextContentController {

  @Autowired
  private TextContentService textContentService;

  @PostMapping
  public ResponseEntity<TextContent> createTextContent(@RequestBody TextContent textContent) {
    return Try.of(() -> textContentService.createTextContent(textContent))
        .map(ResponseEntity::ok)
        .recover(TextContentExistException.class,
            throwable -> ResponseEntity.status(HttpStatus.CONFLICT).build())
        .getOrElseThrow(
            throwable -> new RuntimeException("Unknown error" + throwable.getMessage(), throwable));
  }

  @GetMapping("/{key}")
  public ResponseEntity<TextContent> findTextContent(@PathVariable("key") String key) {
    return textContentService.findTextContentByKey(key)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
