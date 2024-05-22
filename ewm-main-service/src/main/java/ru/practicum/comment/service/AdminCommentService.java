package ru.practicum.comment.service;

import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;

public interface AdminCommentService {
    void deleteComment(Long id);

    TotalCommentDto updateComment(Long id, CommentDto commentDto);
}
