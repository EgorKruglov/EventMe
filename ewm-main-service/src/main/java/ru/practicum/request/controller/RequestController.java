package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RestController
@Validated
@RequestMapping("/users/")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping("/{userId}/requests")
    public ResponseEntity<RequestDto> addRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        RequestDto result = requestService.addRequest(userId, eventId);
        log.info("Добавлен запрос на участие в мероприятии");
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getRequests(@PathVariable Long userId) {
        List<RequestDto> result = requestService.getAllRequests(userId);
        log.info("Отправлен список запросов на участии в мероприятиях пользователя id:{}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> canselRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        RequestDto result = requestService.canselRequest(userId, requestId);
        log.info("Отмена запроса на участие в мероприятии пользователем id:{}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
