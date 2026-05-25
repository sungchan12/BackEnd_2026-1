package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Optional<Board> findById(Long id) {
        return boardRepository.findById(id);
    }

    public Board create(Board request) {
        Board board = new Board(null, request.getName());
        return boardRepository.save(board);
    }

    public Optional<Board> update(Long id, Board request) {
        return boardRepository.findById(id).map(board -> {
            if (request.getName() != null) board.setName(request.getName());
            return boardRepository.save(board);
        });
    }

    public boolean delete(Long id) {
        if (!boardRepository.existsById(id)) {
            return false;
        }
        boardRepository.deleteById(id);
        return true;
    }
}