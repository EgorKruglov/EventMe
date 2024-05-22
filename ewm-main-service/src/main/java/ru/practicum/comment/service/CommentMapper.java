package ru.practicum.comment.service;

import lombok.experimental.UtilityClass;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static TotalCommentDto toTotalCommentDto(Comment comment) {
        return TotalCommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(UserMapper.toUserDto(comment.getAuthor()))
                .created(comment.getCreated())
                .lastUpdatedOn(comment.getLastUpdatedOn())
                .build();
    }

    public static List<TotalCommentDto> listToTotalDtoList(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toTotalCommentDto).collect(Collectors.toList());
    }

    public static Comment toComment(CommentDto commentDto, Event event, User user) {
        return Comment.builder()
                .text(commentDto.getText())
                .event(event)
                .author(user)
                .created(LocalDateTime.now())
                .lastUpdatedOn(LocalDateTime.now())
                .build();
    }
}
