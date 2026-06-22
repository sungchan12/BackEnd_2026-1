package com.example.demo.dao;

import com.example.demo.domain.Article;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class ArticleDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Article> rowMapper = (rs, rowNum) -> new Article(
            rs.getLong("id"),
            rs.getObject("author_id", Long.class),
            rs.getObject("board_id", Long.class),
            rs.getString("title"),
            rs.getString("content"),
            rs.getTimestamp("created_date").toLocalDateTime(),
            rs.getTimestamp("modified_date").toLocalDateTime()
    );

    public ArticleDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Article> findAll() {
        return jdbcTemplate.query("SELECT * FROM article", rowMapper);
    }

    public Optional<Article> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM article WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    public List<Article> findByBoardId(Long boardId) {
        return jdbcTemplate.query("SELECT * FROM article WHERE board_id = ?", rowMapper, boardId);
    }

    public Article save(Article article) {
        if (article.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO article (author_id, board_id, title, content, created_date, modified_date) VALUES (?, ?, ?, ?, ?, ?)",
                        new String[]{"id"});
                ps.setObject(1, article.getAuthorId());
                ps.setObject(2, article.getBoardId());
                ps.setString(3, article.getTitle());
                ps.setString(4, article.getContent());
                ps.setTimestamp(5, Timestamp.valueOf(article.getCreatedAt()));
                ps.setTimestamp(6, Timestamp.valueOf(article.getUpdatedAt()));
                return ps;
            }, keyHolder);
            article.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(
                    "UPDATE article SET author_id = ?, board_id = ?, title = ?, content = ?, modified_date = ? WHERE id = ?",
                    article.getAuthorId(), article.getBoardId(),
                    article.getTitle(), article.getContent(),
                    Timestamp.valueOf(article.getUpdatedAt()), article.getId());
        }
        return article;
    }

    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM article WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM article WHERE id = ?", id);
    }
}