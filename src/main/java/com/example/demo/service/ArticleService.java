package com.example.demo.service;

import com.example.demo.dao.ArticleDao;
import com.example.demo.dao.BoardDao;
import com.example.demo.dao.MemberDao;
import com.example.demo.domain.Article;
import com.example.demo.exception.InvalidReferenceException;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleDao articleDao;
    private final MemberDao memberDao;
    private final BoardDao boardDao;

    public ArticleService(ArticleDao articleDao, MemberDao memberDao, BoardDao boardDao) {
        this.articleDao = articleDao;
        this.memberDao = memberDao;
        this.boardDao = boardDao;
    }

    public List<Article> findAll() {
        return articleDao.findAll();
    }

    public Article findById(Long id) {
        return articleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시물입니다: " + id));
    }

    public List<Article> findByBoardId(Long boardId) {
        return articleDao.findByBoardId(boardId);
    }

    @Transactional
    public Article create(Article request) {
        LocalDateTime now = LocalDateTime.now();
        return articleDao.save(new Article(
                null, request.getAuthorId(), request.getBoardId(),
                request.getTitle(), request.getContent(), now, now));
    }

    @Transactional
    public Article update(Long id, Article request) {
        Article article = findById(id);
        if (request.getAuthorId() != null && !memberDao.existsById(request.getAuthorId())) {
            throw new InvalidReferenceException("존재하지 않는 사용자입니다: " + request.getAuthorId());
        }
        if (request.getBoardId() != null && !boardDao.existsById(request.getBoardId())) {
            throw new InvalidReferenceException("존재하지 않는 게시판입니다: " + request.getBoardId());
        }
        if (request.getAuthorId() != null) article.setAuthorId(request.getAuthorId());
        if (request.getBoardId() != null) article.setBoardId(request.getBoardId());
        if (request.getTitle() != null) article.setTitle(request.getTitle());
        if (request.getContent() != null) article.setContent(request.getContent());
        article.setUpdatedAt(LocalDateTime.now());
        return articleDao.save(article);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        articleDao.deleteById(id);
    }
}