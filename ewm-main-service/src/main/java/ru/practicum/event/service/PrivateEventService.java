package ru.practicum.event.service;

import ru.practicum.event.model.dto.PrivateEventRequestDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.model.dto.UpdateEventDto;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.model.dto.RequestShortDto;
import ru.practicum.request.model.dto.RequestUpdateDto;

import java.util.List;

public interface PrivateEventService {
    TotalEventDto addEvent(Long userId, PrivateEventRequestDto eventDto);

    List<TotalEventDto> getEventByUserId(Long userId, int from, int size);

    TotalEventDto updateEvent(Long userId, Long eventId, UpdateEventDto eventDto);

    TotalEventDto getEventByUserIdAndEventId(Long userId, Long eventId);

    List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId);

    RequestUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto);
}
