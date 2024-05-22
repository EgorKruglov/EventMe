package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.service.PrivateCommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/comments")
public class PrivateCommentController {
    private final PrivateCommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<TotalCommentDto> addComment(@PathVariable Long userId,
                                                      @PathVariable Long eventId,
                                                      @RequestBody @Validated(Validator.Create.class) CommentDto commentDto) {
        TotalCommentDto result = commentService.addComment(userId, eventId, commentDto);
        log.info("Пользователь id:{} добавил комментарий {}", userId, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/users/{userId}/{commentId}")
    public ResponseEntity<TotalCommentDto> getComment(@PathVariable Long userId,
                                                      @PathVariable Long commentId) {
        TotalCommentDto result = commentService.getComment(userId, commentId);
        log.info("Пользователю отправлен его комментарий");
        return ResponseEntity.ok().body(result);
    }

    @PatchMapping("/users/{userId}/{commentId}")
    public ResponseEntity<TotalCommentDto> updateComment(@PathVariable Long userId,
                                                         @PathVariable Long commentId,
                                                         @RequestBody @Validated({Validator.Update.class}) CommentDto commentDto) {
        TotalCommentDto result = commentService.updateComment(userId, commentId, commentDto);
        log.info("Обновлён комментарий id:{}", commentId);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/users/{userId}/comments")
    public ResponseEntity<List<TotalCommentDto>> getUserComments(@PathVariable Long userId,
                                                                 @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                 @RequestParam(value = "size", defaultValue = "10") Integer size) {
        List<TotalCommentDto> result = commentService.getUserComments(userId, from, size);
        log.info("Отправлен список всех комментариев пользователя");
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/users/{userId}/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long userId,
                                                @PathVariable Long commentId) {
        commentService.deleteComment(userId, commentId);
        log.info("Пользователь id:{} удалили комментарий", userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Комментарий удалён");
    }

    @GetMapping("/search")
    public ResponseEntity<List<TotalCommentDto>> getCommentsBySearch(@RequestParam(name = "text") String text,
                                                                     @RequestParam(value = "from", defaultValue = "0") Integer from,
                                                                     @RequestParam(value = "size", defaultValue = "10") Integer size) {
        List<TotalCommentDto> result = commentService.getCommentsBySearch(text, from, size);
        log.info("Отправлен список комментариев по поисковому запросу");
        return ResponseEntity.ok().body(result);
    }
}
