package com.example.demo.controller;

import com.example.demo.domain.Article;
import com.example.demo.dto.ArticleResponse;
import com.example.demo.service.ArticleService;
import com.example.demo.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ArticleController {

    private final ArticleService articleService;
    private final MemberService memberService;

    public ArticleController(ArticleService articleService, MemberService memberService) {
        this.articleService = articleService;
        this.memberService = memberService;
    }

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<ArticleResponse> responses = articleService.findAll().stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> getArticle(@PathVariable Long id) {
        return articleService.findById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article request) {
        Article created = articleService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article request) {
        return articleService.update(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (!articleService.delete(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    private ArticleResponse toResponse(Article article) {
        String author = Optional.ofNullable(article.getAuthorId())
                .flatMap(memberService::findById)
                .map(m -> m.getName())
                .orElse("알 수 없음");
        return new ArticleResponse(article.getTitle(), author, article.getCreatedAt(), article.getContent());
    }
}