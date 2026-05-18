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
    // 특정 article 조회
    @GetMapping("/article/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable Long id) {
        Article article = store.get(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }
    // 특정 article 수정
    @PutMapping("/article/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article request) {
        Article article = store.get(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        article.setTitle(request.getTitle());
        article.setContent(request.getContent());
        return ResponseEntity.ok(article);
    }
}