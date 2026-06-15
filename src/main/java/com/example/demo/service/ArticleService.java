package com.example.demo.service;

import com.example.demo.domain.Article;
import com.example.demo.exception.InvalidReferenceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ArticleRepository;
import com.example.demo.repository.BoardRepository;
import com.example.demo.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    public ArticleService(ArticleRepository articleRepository,
                          MemberRepository memberRepository,
                          BoardRepository boardRepository) {
        this.articleRepository = articleRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다: " + id));
    }

    public List<Article> findByBoardId(Long boardId) {
        return articleRepository.findByBoardId(boardId);
    }

    public Article create(Article request) {
        LocalDateTime now = LocalDateTime.now();
        return articleRepository.save(new Article(
                null, request.getAuthorId(), request.getBoardId(),
                request.getTitle(), request.getContent(), now, now));
    }

    public Article update(Long id, Article request) {
        Article article = findById(id);
        if (request.getAuthorId() != null && !memberRepository.existsById(request.getAuthorId())) {
            throw new InvalidReferenceException("존재하지 않는 사용자입니다: " + request.getAuthorId());
        }
        if (request.getBoardId() != null && !boardRepository.existsById(request.getBoardId())) {
            throw new InvalidReferenceException("존재하지 않는 게시판입니다: " + request.getBoardId());
        }
        if (request.getAuthorId() != null) article.setAuthorId(request.getAuthorId());
        if (request.getBoardId() != null) article.setBoardId(request.getBoardId());
        if (request.getTitle() != null) article.setTitle(request.getTitle());
        if (request.getContent() != null) article.setContent(request.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        return articleRepository.save(article);
    }

    public void delete(Long id) {
        findById(id);
        articleRepository.deleteById(id);
    }
}