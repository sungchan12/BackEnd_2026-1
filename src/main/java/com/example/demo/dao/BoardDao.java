package com.example.demo.dao;

import com.example.demo.domain.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class BoardDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Board> rowMapper = (rs, rowNum) -> new Board(
            rs.getLong("id"),
            rs.getString("name")
    );

    public BoardDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Board> findAll() {
        return jdbcTemplate.query("SELECT * FROM board", rowMapper);
    }

    public Optional<Board> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM board WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    public Board save(Board board) {
        if (board.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO board (name) VALUES (?)",
                        new String[]{"id"});
                ps.setString(1, board.getName());
                return ps;
            }, keyHolder);
            board.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update("UPDATE board SET name = ? WHERE id = ?",
                    board.getName(), board.getId());
        }
        return board;
    }

    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM board WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM board WHERE id = ?", id);
    }
}