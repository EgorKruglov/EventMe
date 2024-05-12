package ru.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.PrivateEventRequestDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.event.model.dto.UpdateEventDto;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventMapper;
import ru.practicum.event.service.LocationService;
import ru.practicum.event.service.PrivateEventService;
import ru.practicum.event.status.State;
import ru.practicum.event.status.UserEventStatus;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.exceptions.extraExceptions.NotFoundException;
import ru.practicum.exceptions.extraExceptions.ValidationException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.model.dto.RequestShortDto;
import ru.practicum.request.model.dto.RequestUpdateDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.service.RequestMapper;
import ru.practicum.request.status.Status;
import ru.practicum.statsService.StatsService;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final LocationService locationService;
    private final StatsService statsService;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Transactional
    @Override
    public TotalEventDto addEvent(Long userId, PrivateEventRequestDto eventDto) {
        log.info("Добавление нового мероприятия");

        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ConflictException("Начало мероприятие не может быть раньше, чем через 2 часа");
        }

        Event event = EventMapper.privatetoEvent(eventDto);

        event.setCategory(categoryRepository.findById(event.getCategory().getId()).orElseThrow(()
                -> new NotFoundException("Категория мероприятий не найдена")));
        event.setState(State.PENDING);
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setLocation(locationService.addLocation(event.getLocation()));
        event.setInitiator(userRepository.findById(userId).orElseThrow(()
                -> new NotFoundException("Пользователь не найден")));

        return EventMapper.toTotalDto(eventRepository.save(event));
    }

    @Transactional
    @Override
    public List<TotalEventDto> getEventByUserId(Long userId, int from, int size) {
        log.info("Получение данных о мероприятии");
        List<Event> events = eventRepository.findAllByInitiatorId(userId, PageRequest.of(from, size, Sort.Direction.ASC, "id"));

        if (events.isEmpty()) {
            return List.of();
        }

        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(events);
        Map<Long, Long> mapView = statsService.toView(events);

        for (Event event : events) {
            event.setConfirmedRequests(confirmedRequest.getOrDefault(event.getId(), 0L));
            event.setView(mapView.getOrDefault(event.getId(), 0L));
        }
        return EventMapper.eventListToTotalDtoList(events);
    }

    @Transactional
    @Override
    public TotalEventDto updateEvent(Long userId, Long eventId, UpdateEventDto eventDto) {
        log.info("Обновление данных о мероприятии");
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Редактировать данные о мероприятии можно только его создателям");
        }

        if (event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Мероприятие невозможно редактировать после публикации");
        }

        LocalDateTime eventTime = eventDto.getEventDate();
        if (eventTime != null) {
            if (eventTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ConflictException("Начало мероприятие не может быть раньше, чем через 2 часа");
            }
            if (eventTime.isBefore(LocalDateTime.now())) {
                throw new ValidationException("Дата мероприятия не может быть в прошлом");
            }
            event.setEventDate(eventTime);
        }

        UserEventStatus status = eventDto.getStateAction();
        if (status != null) {
            if (status.equals(UserEventStatus.SEND_TO_REVIEW)) {
                event.setState(State.PENDING);
            }
            if (status.equals(UserEventStatus.CANCEL_REVIEW)) {
                event.setState(State.CANCELED);
            }
        }

        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }
        if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
            event.setTitle(eventDto.getTitle());
        }
        if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getLocation() != null) {
            event.setLocation(locationService.getLocation(eventDto.getLocation()).orElse(locationService.addLocation(eventDto.getLocation())));
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoryRepository.findById(eventDto.getCategory())
                    .orElseThrow(() -> new NotFoundException("Категория мероприятий не найдена")));
        }

        Map<Long, Long> view = statsService.toView(List.of(event));
        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));

        event.setView(view.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));
        return EventMapper.toTotalDto(event);
    }

    @Transactional
    @Override
    public TotalEventDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        log.info("обновление данных мероприятия");
        Event event = eventRepository.findByIdAndInitiatorId(eventId, userId).orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        Map<Long, Long> confirmedRequest = statsService.toConfirmedRequest(List.of(event));
        Map<Long, Long> mapView = statsService.toView(List.of(event));

        event.setView(mapView.getOrDefault(eventId, 0L));
        event.setConfirmedRequests(confirmedRequest.getOrDefault(eventId, 0L));

        return EventMapper.toTotalDto(event);
    }

    @Transactional
    @Override
    public List<RequestDto> getRequestByUserIdAndEventId(Long userId, Long eventId) {
        log.info("Получение списка запросов на мероприятие пользователя");
        if (!eventRepository.existsByIdAndInitiatorId(eventId, userId)) {
            throw new ConflictException("Запрашивать особые данные о мероприятии можно только его создателям");
        }
        List<Request> requests = requestRepository.findAllByEventId(eventId);
        return RequestMapper.listToDtoList(requests);
    }

    @Transactional
    @Override
    public RequestUpdateDto updateRequestByOwner(Long userId, Long eventId, RequestShortDto requestShortDto) {
        log.info("Обновление списка запросов на мероприятие пользователя");
        RequestShortDto requestShort = RequestMapper.toRequestShort(requestShortDto);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Редактировать данные о мероприятии можно только его создателям");
        }

        int confirmedRequest = statsService.toConfirmedRequest(List.of(event)).values().size();

        if (event.getParticipantLimit() != 0 && confirmedRequest >= event.getParticipantLimit()) {
            throw new ConflictException("Список участников в мероприятии заполнен");
        }

        RequestUpdateDto updateRequest = new RequestUpdateDto();

        requestShort.getRequestIds().forEach(requestId -> {
            Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

            if (requestShort.getStatus().equals(Status.CONFIRMED)) {
                request.setStatus(Status.CONFIRMED);
                updateRequest.getConfirmedRequests().add(RequestMapper.toRequestDto(request));
            }
            if (requestShort.getStatus().equals(Status.REJECTED)) {
                request.setStatus(Status.REJECTED);
                updateRequest.getRejectedRequests().add(RequestMapper.toRequestDto(request));
            }
        });
        return updateRequest;
    }
}
