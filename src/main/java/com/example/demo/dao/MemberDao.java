package com.example.demo.dao;

import com.example.demo.domain.Member;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class MemberDao {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Member> rowMapper = (rs, rowNum) -> new Member(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password")
    );

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Member> findAll() {
        return jdbcTemplate.query("SELECT * FROM member", rowMapper);
    }

    public Optional<Member> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM member WHERE id = ?", rowMapper, id)
                .stream().findFirst();
    }

    public Optional<Member> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM member WHERE email = ?", rowMapper, email)
                .stream().findFirst();
    }

    public Member save(Member member) {
        if (member.getId() == null) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO member (name, email, password) VALUES (?, ?, ?)",
                        new String[]{"id"});
                ps.setString(1, member.getName());
                ps.setString(2, member.getEmail());
                ps.setString(3, member.getPassword());
                return ps;
            }, keyHolder);
            member.setId(keyHolder.getKey().longValue());
        } else {
            jdbcTemplate.update(
                    "UPDATE member SET name = ?, email = ?, password = ? WHERE id = ?",
                    member.getName(), member.getEmail(), member.getPassword(), member.getId());
        }
        return member;
    }

    public boolean existsById(Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM member WHERE id = ?", Integer.class, id);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM member WHERE email = ?", Integer.class, email);
        return count != null && count > 0;
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM member WHERE id = ?", id);
    }
}