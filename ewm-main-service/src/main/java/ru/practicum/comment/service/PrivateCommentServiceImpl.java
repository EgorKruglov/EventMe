package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.repository.CommentRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.State;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.exceptions.extraExceptions.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrivateCommentServiceImpl implements PrivateCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public TotalCommentDto addComment(Long userId, Long eventId, CommentDto commentDto) {
        log.info("Добавление комментария пользователем id:{}", userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Мероприятие не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Невозможно добавить комментарий к неопубликованному событию");
        }
        return CommentMapper.toTotalCommentDto(commentRepository.save(CommentMapper.toComment(commentDto, event, user)));
    }

    @Transactional(readOnly = true)
    @Override
    public TotalCommentDto getComment(Long userId, Long commentId) {
        log.info("Запрос комментария пользователем id:{}", userId);
        return CommentMapper.toTotalCommentDto(commentRepository.findByAuthorIdAndId(userId, commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден")));
    }

    @Override
    public TotalCommentDto updateComment(Long userId, Long commentId, CommentDto commentDto) {
        log.info("Обновление комментария пользователем id:{}", userId);
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Не автор комментария");
        }

        LocalDateTime thisTime = LocalDateTime.now();
        if (thisTime.isAfter(comment.getCreated().plusHours(1L))) {
            throw new ConflictException("Нет возможности редактировать");
        }

        comment.setText(commentDto.getText());
        comment.setLastUpdatedOn(thisTime);
        return CommentMapper.toTotalCommentDto(comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TotalCommentDto> getUserComments(Long userId, Integer from, Integer size) {
        log.info("Запрос пользователя на получение списка своих комментариев");
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь не найден"));
        List<Comment> comments = commentRepository.findByAuthorId(userId, PageRequest.of(from / size, size));
        return CommentMapper.listToTotalDtoList(comments);
    }

    @Override
    public void deleteComment(Long userId, Long commentId) {
        log.info("Удаление комментария пользователем id:{}", userId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Комментарий не найден"));
        if (!Objects.equals(comment.getAuthor().getId(), userId)) {
            throw new ConflictException("Не автор комментария");
        }
        commentRepository.deleteById(commentId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TotalCommentDto> getCommentsBySearch(String text, Integer from, Integer size) {
        log.info("Поиск по комментариям");
        List<Comment> comments = commentRepository.findCommentListByText(text, PageRequest.of(from / size, size));
        return CommentMapper.listToTotalDtoList(comments);
    }
}
