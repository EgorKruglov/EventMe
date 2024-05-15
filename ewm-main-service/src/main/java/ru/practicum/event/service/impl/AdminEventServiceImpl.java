package ru.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventAdminDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.AdminEventService;
import ru.practicum.event.service.EventMapper;
import ru.practicum.event.service.LocationService;
import ru.practicum.event.status.AdminEventStatus;
import ru.practicum.event.status.State;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.exceptions.extraExceptions.NotFoundException;
import ru.practicum.exceptions.extraExceptions.ValidationException;
import ru.practicum.statsService.StatsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationService locationService;
    private final StatsService statsService;

    @Override
    public TotalEventDto updateEventByAdmin(Long eventId, EventAdminDto eventDto) {
        log.info("Обновление мероприятия администратором");
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Событие не найдено id:" + eventId));

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ConflictException("Нельзя изменять мероприятие после его начала");
        }

        if (eventDto.getEventDate() != null) {
            if (eventDto.getEventDate().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Некорректная дата события");
            } else {
                event.setEventDate(eventDto.getEventDate());
            }
        }

        if (eventDto.getStateAction() != null) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ConflictException("Ошибка валидации статуса мероприятия");
            }

            if (eventDto.getStateAction().equals(AdminEventStatus.PUBLISH_EVENT)) {
                event.setState(State.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now().withNano(0));
            }
            if (eventDto.getStateAction().equals(AdminEventStatus.REJECT_EVENT)) {
                event.setState(State.CANCELED);
            }
        }

        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(locationService.getLocation(eventDto.getLocation()).orElse(locationService.addLocation(eventDto.getLocation())));
        }
        if (eventDto.getAnnotation() != null && !eventDto.getTitle().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория мероприятий не найдена")));
        }

        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        Map<Long, Long> view = statsService.toView(List.of(event));

        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        return EventMapper.toTotalDto(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TotalEventDto> getEventsByAdmin(List<Long> users, List<State> states, List<Long> categories, LocalDateTime start, LocalDateTime end, int from, int size) {
        log.info("Получение списка о мероприятии");
        List<Event> events = eventRepository.findAllByParam(users, states, categories, start, end, PageRequest.of(from, size, Sort.Direction.ASC, "id"));

        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(events);
        Map<Long, Long> view = statsService.toView(events);
        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(view.getOrDefault(event.getId(), 0L));
        }
        return EventMapper.eventListToTotalDtoList(events);
    }
}


