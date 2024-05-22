package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminCommentServiceImpl implements AdminCommentService {
    private final CommentRepository commentRepository;

    @Override
    public void deleteComment(Long id) {
        log.info("Удаление комментария администратором");
        commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Комментарий не найден id:" + id));
        commentRepository.deleteById(id);
    }

    @Override
    public TotalCommentDto updateComment(Long id, CommentDto commentDto) {
        log.info("Обновление комментария администратором");
        Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Комментарий не найден id:" + id));

        if (commentDto.getText() != null) {
            comment.setText(commentDto.getText());
        }
        comment.setLastUpdatedOn(LocalDateTime.now());
        return CommentMapper.toTotalCommentDto(comment);
    }
}
