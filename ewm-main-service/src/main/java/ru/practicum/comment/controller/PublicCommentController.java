package ru.practicum.comment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.comment.model.dto.TotalCommentDto;
import ru.practicum.comment.service.PublicCommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/comments")
public class PublicCommentController {
    private final PublicCommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<List<TotalCommentDto>> getCommentsByEvent(@PathVariable Long id,
                                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        List<TotalCommentDto> result = commentService.getCommentsByEvent(id, from, size);
        log.info("Отправлен список комментариев у события id:{}", id);
        return ResponseEntity.ok().body(result);
    }
}
