package com.example.mainservice.controller;

import com.example.mainservice.model.CommentDto;
import com.example.mainservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {

        return commentService.getCommentById(commentId);
    }
}

