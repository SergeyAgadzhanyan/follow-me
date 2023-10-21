package com.example.mainservice.service;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.BadRequestException;
import com.example.mainservice.exception.Messages;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CommentMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CommentRepository;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentDto addComment(Long eventId, Long userId, CommentCreateDto commentCreateDto) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        if (!event.getState().equals(StatEnum.PUBLISHED))
            throw new BadRequestException(Messages.EVENT_NOT_PUBLISHED.getMessage());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        return commentMapper.mapToCommentDto(commentRepository.save(commentMapper.mapToComment(commentCreateDto, event, user)));
    }

    public CommentDto updateComment(Long userId, Long eventId, Long commentId, CommentCreateDto commentCreateDto) {
        Comment comment = getComment(userId, eventId, commentId);
        if (!comment.getCreatedOn().isAfter(LocalDateTime.now().minusHours(1)))
            throw new BadRequestException(Messages.UPDATE_TIME_HAS_EXPIRED.getMessage());
        if (commentCreateDto.getText().equals(comment.getText())) return commentMapper.mapToCommentDto(comment);
        comment.setUpdatedOn(LocalDateTime.now());
        comment.setText(commentCreateDto.getText());
        return commentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    public void deleteCommentByUser(Long userId, Long eventId, Long commentId) {
        getComment(userId, eventId, commentId);
        commentRepository.deleteById(commentId);
    }

    public void deleteCommentByAdmin(Long eventId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        if (!comment.getEvent().getId().equals(eventId))
            throw new BadRequestException(Messages.BAD_REQUEST.getMessage());
        commentRepository.deleteById(commentId);
    }

    public CommentDto getCommentById(Long commentId) {
        return commentMapper.mapToCommentDto(commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage())));
    }

    private Comment getComment(Long userId, Long eventId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        if (!comment.getEvent().getInitiator().getId().equals(userId) && !comment.getUser().getId().equals(userId) || !comment.getEvent().getId().equals(eventId))
            throw new BadRequestException(Messages.DONT_HAVE_ENOUGH_RIGHTS.getMessage());
        return comment;
    }
}
