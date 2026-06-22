package com.example.demo.dao;

import com.example.demo.domain.Article;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ArticleDao {

    @PersistenceContext
    private EntityManager em;

    public List<Article> findAll() {
        return em.createQuery("SELECT a FROM Article a", Article.class)
                .getResultList();
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(em.find(Article.class, id));
    }

    public List<Article> findByBoardId(Long boardId) {
        return em.createQuery("SELECT a FROM Article a WHERE a.boardId = :boardId", Article.class)
                .setParameter("boardId", boardId)
                .getResultList();
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            em.persist(article);
            return article;
        }
        return em.merge(article);
    }

    public boolean existsById(Long id) {
        Long count = em.createQuery("SELECT COUNT(a) FROM Article a WHERE a.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }

    public void deleteById(Long id) {
        Article article = em.find(Article.class, id);
        if (article != null) {
            em.remove(article);
        }
    }
}