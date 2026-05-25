package com.example.demo.dto;

import java.time.LocalDateTime;

public record ArticleResponse(String title, String author, LocalDateTime date, String content) {}