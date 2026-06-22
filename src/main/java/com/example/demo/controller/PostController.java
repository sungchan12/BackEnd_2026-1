package com.example.demo.controller;

import com.example.demo.domain.Article;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {

    private final ArticleService articleService;
    private final BoardService boardService;

    public PostController(ArticleService articleService, BoardService boardService) {
        this.articleService = articleService;
        this.boardService = boardService;
    }

    @GetMapping("/posts")
    public String getPosts(@RequestParam Long boardId, Model model) {
        String boardName = boardService.findById(boardId).getName();
        List<Article> articles = articleService.findByBoardId(boardId);

        model.addAttribute("boardName", boardName);
        model.addAttribute("articles", articles);
        return "posts";
    }
}