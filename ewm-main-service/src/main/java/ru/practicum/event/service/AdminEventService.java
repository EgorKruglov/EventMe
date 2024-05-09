package ru.practicum.event.service;

import ru.practicum.event.model.dto.EventAdminDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.status.State;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminEventService {
    TotalEventDto updateEventByAdmin(Long eventId, EventAdminDto eventDto);

    List<TotalEventDto> getEventsByAdmin(List<Long> users, List<State> states, List<Long> categories,
                                          LocalDateTime start, LocalDateTime end, int from, int size);
}
