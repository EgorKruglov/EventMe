package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.comment.model.dto.CommentDto;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.service.AdminCommentService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final AdminCommentService commentService;

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable @Positive Long id) {
        commentService.deleteComment(id);
        log.info("Удален комментарий id:{} администратором ", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Комментарий удалён id:{}" + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TotalCommentDto> updateComment(@PathVariable Long id,
                                                         @RequestBody @Validated({Validator.Update.class}) CommentDto commentDto) {
        TotalCommentDto result = commentService.updateComment(id, commentDto);
        log.info("Комментарий id:{} обновлён администратором", id);
        return ResponseEntity.ok().body(result);
    }
}
