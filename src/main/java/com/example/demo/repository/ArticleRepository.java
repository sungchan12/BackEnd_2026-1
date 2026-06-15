package com.example.demo.repository;

import com.example.demo.domain.Article;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ArticleRepository {

    private final Map<Long, Article> store;
    private final AtomicLong sequence = new AtomicLong(5);

    public ArticleRepository() {
        LocalDateTime createdAt = LocalDateTime.now().minusHours(3);
        store = new HashMap<>(Map.of(
                1L, new Article(1L, 1L, 1L, "제목0", "", createdAt, createdAt),
                2L, new Article(2L, 2L, 1L, "제목1", "내용입니다!!", createdAt, createdAt),
                3L, new Article(3L, 3L, 1L, "제목2", "내용입니다!!내용입니다!!", createdAt, createdAt),
                4L, new Article(4L, 4L, 1L, "제목3", "내용입니다!!내용입니다!!내용입니다!!", createdAt, createdAt)
        ));
    }

    public List<Article> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Article> findByBoardId(Long boardId) {
        return store.values().stream()
            .filter(a -> boardId.equals(a.getBoardId()))
            .collect(Collectors.toList());
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            article.setId(sequence.getAndIncrement());
        }
        store.put(article.getId(), article);
        return article;
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}