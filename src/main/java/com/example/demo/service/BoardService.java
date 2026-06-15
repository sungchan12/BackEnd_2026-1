package com.example.demo.service;

import com.example.demo.domain.Board;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시판입니다: " + id));
    }

    public Board create(Board request) {
        return boardRepository.save(new Board(null, request.getName()));
    }

    public Board update(Long id, Board request) {
        Board board = findById(id);
        if (request.getName() != null) board.setName(request.getName());
        return boardRepository.save(board);
    }

    public void delete(Long id) {
        findById(id);
        boardRepository.deleteById(id);
    }
}