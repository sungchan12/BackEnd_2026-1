package com.example.demo.service;

import com.example.demo.dao.BoardDao;
import com.example.demo.domain.Board;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardDao boardDao;

    public BoardService(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    public List<Board> findAll() {
        return boardDao.findAll();
    }

    public Board findById(Long id) {
        return boardDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 게시판입니다: " + id));
    }

    @Transactional
    public Board create(Board request) {
        return boardDao.save(new Board(null, request.getName()));
    }

    @Transactional
    public Board update(Long id, Board request) {
        Board board = findById(id);
        if (request.getName() != null) board.setName(request.getName());
        return boardDao.save(board);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        boardDao.deleteById(id);
    }
}