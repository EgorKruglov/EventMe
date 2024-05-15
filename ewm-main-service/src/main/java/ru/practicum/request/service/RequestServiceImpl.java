package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.status.State;
import ru.practicum.exceptions.extraExceptions.ConflictException;
import ru.practicum.exceptions.extraExceptions.NotFoundException;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.EventConfirmedRequests;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.repository.RequestRepository;
import ru.practicum.request.status.Status;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public RequestDto addRequest(Long userId, Long eventId) {
        log.info("Запрос на добавление запроса на мероприятие пользователем id:{}", userId);
        Status status;
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Мероприятие не найдено"));

        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Создатель мероприятия уже в нём участвует");
        }

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Мероприятие должно быть опубликовано");
        }

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Пользователь уже участвует в мероприятии");
        }

        status = (!event.getRequestModeration() || event.getParticipantLimit() == 0) ? Status.CONFIRMED : Status.PENDING;

        List<EventConfirmedRequests> confirmed = requestRepository.countByEventId(List.of(eventId));

        if (confirmed.size() >= event.getParticipantLimit() && event.getParticipantLimit() != 0) {
            throw new ConflictException("Список участников полон");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Request request = requestRepository.save(Request.builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now().withNano(0))
                .status(status)
                .build());

        return RequestMapper.toRequestDto(request);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests(Long userId) {
        log.info("Запрос на получение всех запросов на мероприятия пользователем id:{}", userId);
        return RequestMapper.listToDtoList(requestRepository.findAllByRequesterId(userId));
    }

    @Override
    public RequestDto canselRequest(Long userId, Long requestId) {
        log.info("Запрос на отмену запроса на участие в мероприятии пользователем id:{}", userId);
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundException("Запрос не найден"));

        request.setStatus(Status.CANCELED);
        return RequestMapper.toRequestDto(request);
    }
}
