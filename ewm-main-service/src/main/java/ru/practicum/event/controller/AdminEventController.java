package ru.practicum.event.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.event.model.dto.EventAdminDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.event.status.State;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping("/admin/events")
@Slf4j
public class AdminEventController {
    private final AdminEventService eventService;

    @PatchMapping("/{id}")
    public ResponseEntity<TotalEventDto> updateEvent(@PathVariable Long id,
                                                     @RequestBody @Validated({Validator.Update.class}) EventAdminDto eventDto) {
        TotalEventDto result = eventService.updateEventByAdmin(id, eventDto);
        log.info("Данные мероприятия обновлены администратором {}", result);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping
    public ResponseEntity<List<TotalEventDto>> getEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                                @RequestParam(required = false) List<State> states,
                                                                @RequestParam(required = false) List<Long> categories,
                                                                @RequestParam(required = false)
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                                @RequestParam(required = false)
                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                @RequestParam(defaultValue = "10") @Positive int size) {
        List<TotalEventDto> result = eventService.getEventsByAdmin(users, states, categories, start, end, from, size);
        log.info("Отправлен список мероприятий по запросу администратора");
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
