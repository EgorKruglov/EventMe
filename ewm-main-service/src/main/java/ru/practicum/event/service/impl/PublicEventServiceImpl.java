package ru.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventMapper;
import ru.practicum.event.service.PublicEventService;
import ru.practicum.event.status.State;
import ru.practicum.exceptions.extraExceptions.NotFoundException;
import ru.practicum.exceptions.extraExceptions.ValidationException;
import ru.practicum.statsService.StatsService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final StatsService statService;

    @Override
    @Transactional
    public TotalEventDto findEvent(Long id, HttpServletRequest request) {
        log.info("Запрос на получение информации о событии id:{}", id);
        Event event = eventRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Событие не найдено Id:" + id));
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException("Запрос на неопубликованное событие id" + id);
        }

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statService.toView(List.of(event));
        statService.addHits(request);

        event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
        event.setView(view.getOrDefault(event.getId(), 0L));

        return EventMapper.toTotalDto(event);
    }

    @Override
    @Transactional
    public List<EventDto> findEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size,
                                     HttpServletRequest request) {
        log.info("Запрос на получение списка событий");
        sort = (sort != null && sort.equals("EVENT_DATE")) ? "eventDate" : "id";

        if (rangeEnd != null && rangeStart != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidationException("Дата окончания события не может быть раньше даты начала");
            }
        }

        List<Event> events = eventRepository.findAllEvents(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, PageRequest.of(from > 0 ? from / size : 0, size, Sort.by(sort).descending()));

        Map<Long, Long> confirmedRequest = statService.toConfirmedRequest(events);
        Map<Long, Long> view = statService.toView(events);

        List<EventDto> result = new ArrayList<>();
        events.forEach(event -> result.add(EventMapper.toDto(event, view.getOrDefault(event.getId(), 0L),
                confirmedRequest.getOrDefault(event.getId(), 0L))));

        statService.addHits(request);
        return result;
    }
}


