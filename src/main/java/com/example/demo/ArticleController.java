package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class ArticleController {

    private final Map<Long, Article> store = new HashMap<>();
    private final AtomicLong primaryKey = new AtomicLong(1);

    // 새로운 article 생성
    @PostMapping("/article")
    public ResponseEntity<Article> createArticle(@RequestBody Article request) {
        Long id = primaryKey.getAndIncrement();
        Article article = new Article(id, request.getTitle(), request.getContent());
        store.put(id, article);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }
}