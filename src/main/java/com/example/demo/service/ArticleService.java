package com.example.demo.service;

import com.example.demo.domain.Article;
import com.example.demo.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Optional<Article> findById(Long id) {
        return articleRepository.findById(id);
    }

    public List<Article> findByBoardId(Long boardId) {
        return articleRepository.findByBoardId(boardId);
    }

    public Article create(Article request) {
        LocalDateTime now = LocalDateTime.now();
        Article article = new Article(
                null,
                request.getAuthorId(),
                request.getBoardId(),
                request.getTitle(),
                request.getContent(),
                now,
                now
        );
        return articleRepository.save(article);
    }

    public Optional<Article> update(Long id, Article request) {
        return articleRepository.findById(id).map(article -> {
            if (request.getAuthorId() != null) article.setAuthorId(request.getAuthorId());
            if (request.getBoardId() != null) article.setBoardId(request.getBoardId());
            if (request.getTitle() != null) article.setTitle(request.getTitle());
            if (request.getContent() != null) article.setContent(request.getContent());
            article.setUpdatedAt(LocalDateTime.now());
            return articleRepository.save(article);
        });
    }

    public boolean delete(Long id) {
        if (!articleRepository.existsById(id)) {
            return false;
        }
        articleRepository.deleteById(id);
        return true;
    }
}