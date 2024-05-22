package ru.practicum.comment.service;

import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;

import java.util.List;

public interface PrivateCommentService {
    TotalCommentDto addComment(Long userId, Long eventId, CommentDto commentDto);

    TotalCommentDto getComment(Long userId, Long commentId);

    TotalCommentDto updateComment(Long userId, Long commentId, CommentDto commentDto);

    void deleteComment(Long userId, Long commentId);

    List<TotalCommentDto> getCommentsBySearch(String text, Integer from, Integer size);

    List<TotalCommentDto> getUserComments(Long userId, Integer from, Integer size);
}
