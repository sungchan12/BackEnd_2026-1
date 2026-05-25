package com.example.demo.repository;

import com.example.demo.domain.Board;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class BoardRepository {

    private final Map<Long, Board> store = new HashMap<>(Map.of(
            1L, new Board(1L, "자유게시판")
    ));
    private final AtomicLong sequence = new AtomicLong(2);

    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Board> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Board save(Board board) {
        if (board.getId() == null) {
            board.setId(sequence.getAndIncrement());
        }
        store.put(board.getId(), board);
        return board;
    }

    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    public void deleteById(Long id) {
        store.remove(id);
    }
}