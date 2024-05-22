package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PublicCommentServiceImpl implements PublicCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<TotalCommentDto> getCommentsByEvent(Long id, Integer from, Integer size) {
        log.info("Отправление списка комментариев мероприятия");
        eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие не найдено id:" + id));

        List<Comment> comments = commentRepository.findAllByEventId(id, PageRequest.of(from / size, size));
        if (comments.isEmpty()) {
            return List.of();
        }
        return CommentMapper.listToTotalDtoList(comments);
    }
}
