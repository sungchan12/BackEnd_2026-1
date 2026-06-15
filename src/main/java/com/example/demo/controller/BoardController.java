package com.example.demo.controller;

import com.example.demo.domain.Board;
import com.example.demo.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards")
    public ResponseEntity<List<Board>> getAllBoards() {
        return ResponseEntity.ok(boardService.findAll());
    }

    @GetMapping("/boards/{id}")
    public ResponseEntity<Board> getBoard(@PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @PostMapping("/boards")
    public ResponseEntity<Board> createBoard(@RequestBody Board request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.create(request));
    }

    @PutMapping("/boards/{id}")
    public ResponseEntity<Board> updateBoard(@PathVariable Long id, @RequestBody Board request) {
        return ResponseEntity.ok(boardService.update(id, request));
    }

    @DeleteMapping("/boards/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}