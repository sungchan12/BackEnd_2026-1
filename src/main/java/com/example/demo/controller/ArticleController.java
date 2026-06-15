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
        return ResponseEntity.ok(toResponse(articleService.findById(id)));
    }

    @PostMapping("/articles")
    public ResponseEntity<Article> createArticle(@RequestBody Article request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(articleService.create(request));
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article request) {
        return ResponseEntity.ok(articleService.update(id, request));
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ArticleResponse toResponse(Article article) {
        String author = Optional.ofNullable(article.getAuthorId())
                .map(authorId -> {
                    try { return memberService.findById(authorId).getName(); }
                    catch (Exception e) { return "알 수 없음"; }
                })
                .orElse("알 수 없음");
        return new ArticleResponse(article.getTitle(), author, article.getCreatedAt(), article.getContent());
    }
}