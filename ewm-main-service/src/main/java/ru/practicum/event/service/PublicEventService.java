package ru.practicum.event.service;

import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.TotalEventDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface PublicEventService {
    List<EventDto> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    TotalEventDto findEvent(Long id, HttpServletRequest request);
}
