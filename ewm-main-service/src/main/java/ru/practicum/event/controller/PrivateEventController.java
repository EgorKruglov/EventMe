package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.event.model.dto.PrivateEventRequestDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.model.dto.UpdateEventDto;
import ru.practicum.event.service.PrivateEventService;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.model.dto.RequestShortDto;
import ru.practicum.request.model.dto.RequestUpdateDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequestMapping(path = "/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final PrivateEventService eventService;

    @PostMapping("/{id}/events")
    public ResponseEntity<TotalEventDto> addEvent(@PathVariable Long id,
                                                  @RequestBody @Validated PrivateEventRequestDto eventDto) {
        TotalEventDto result = eventService.addEvent(id, eventDto);
        log.info("Создано мероприятие {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // todo я так понимаю, чтобы начали работать эндопоинты связанные со статистикой, надо починить тесты из первой части.
    // Они работают, но надо допилить
    @GetMapping("/{id}/events")
    public ResponseEntity<List<TotalEventDto>> getEventsByUserId(@PathVariable Long id,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        List<TotalEventDto> result = eventService.getEventByUserId(id, from, size);
        log.info("Пользователю отправлен список созданных им мероприятий");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public ResponseEntity<TotalEventDto> updateEvent(@PathVariable Long userId,
                                                     @PathVariable Long eventId,
                                                     @RequestBody @Validated(Validator.Update.class) UpdateEventDto eventDto) {
        TotalEventDto result = eventService.updateEvent(userId, eventId, eventDto);
        log.info("Обновлены данные мероприятия {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public ResponseEntity<TotalEventDto> getEventByUserIdAndEventId(@PathVariable Long userId,
                                                                    @PathVariable Long eventId) {
        TotalEventDto result = eventService.getEventByUserIdAndEventId(userId, eventId);
        log.info("Отправлены данные о мероприятии id:{}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<RequestUpdateDto> updateRequestByOwner(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @RequestBody RequestShortDto requestDto) {
        RequestUpdateDto result = eventService.updateRequestByOwner(userId, eventId, requestDto);
        log.info("Обновлены данные о запросе на мероприятие id:{}", eventId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getRequestByUserIdAndEventId(@PathVariable Long userId,
                                                                         @PathVariable Long eventId) {
        List<RequestDto> result = eventService.getRequestByUserIdAndEventId(userId, eventId);
        log.info("Отправлен список запросов на мероприятие пользователя пользователя id:{}", userId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
