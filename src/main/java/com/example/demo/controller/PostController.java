package com.example.demo.controller;

import com.example.demo.domain.Article;
import com.example.demo.service.ArticleService;
import com.example.demo.service.BoardService;
import com.example.demo.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {

    private final ArticleService articleService;
    private final MemberService memberService;
    private final BoardService boardService;

    public PostController(ArticleService articleService,
                          MemberService memberService,
                          BoardService boardService) {
        this.articleService = articleService;
        this.memberService = memberService;
        this.boardService = boardService;
    }

    @GetMapping("/posts")
    public String getPosts(Model model) {
        List<Article> articles = articleService.findAll();

        List<ArticleView> views = articles.stream().map(article -> {
            String authorName = Optional.ofNullable(article.getAuthorId())
                    .flatMap(memberService::findById)
                    .map(m -> m.getName())
                    .orElse("알 수 없음");

            String boardName = Optional.ofNullable(article.getBoardId())
                    .flatMap(boardService::findById)
                    .map(b -> b.getName())
                    .orElse("게시판");

            return new ArticleView(article.getId(), article.getTitle(), article.getContent(),
                    authorName, boardName, article.getCreatedAt());
        }).toList();

        String pageBoardName = views.isEmpty() ? "게시판" : views.get(0).boardName();

        model.addAttribute("boardName", pageBoardName);
        model.addAttribute("articles", views);
        return "posts";
    }

    public record ArticleView(Long id, String title, String content,
                              String authorName, String boardName,
                              LocalDateTime createdAt) {}
}