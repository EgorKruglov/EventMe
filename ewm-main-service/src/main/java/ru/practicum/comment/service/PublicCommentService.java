package ru.practicum.comment.service;

import ru.practicum.comment.model.dto.TotalCommentDto;

import java.util.List;

public interface PublicCommentService {
    List<TotalCommentDto> getCommentsByEvent(Long id, Integer from, Integer size);
}
