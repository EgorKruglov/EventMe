package ru.practicum.statsService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.HitDto;
import ru.practicum.HitResponseDto;
import ru.practicum.StatsClient;
import ru.practicum.event.model.Event;
import ru.practicum.exceptions.extraExceptions.StatisticParsingException;
import ru.practicum.request.model.dto.EventConfirmedRequests;
import ru.practicum.request.repository.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
@Slf4j
public class StatsServiceImpl implements StatsService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RequestRepository requestRepository;

    private final StatsClient statsClient;

    private final ObjectMapper objectMapper;

    @Value("${server.application.name:ewm-service}")
    private String app;

    @Transactional
    @Override
    public Map<Long, Long> toConfirmedRequest(Collection<Event> list) {
        List<Long> listEventId = list.stream().map(Event::getId).collect(Collectors.toList());
        List<EventConfirmedRequests> confirmedRequestShortDtoList = requestRepository.countByEventId(listEventId);
        return confirmedRequestShortDtoList.stream()
                .collect(Collectors.toMap(EventConfirmedRequests::getEventId, EventConfirmedRequests::getConfirmedRequestsCount));
    }

    @Override
    public Map<Long, Long> toView(Collection<Event> events) {
        Map<Long, Long> view = new HashMap<>();
        LocalDateTime start = events.stream().map(Event::getCreatedOn).min(LocalDateTime::compareTo).orElse(null);
        if (start == null) {
            return Map.of();
        }
        List<String> uris = events.stream().map(a -> "/events/" + a.getId()).collect(Collectors.toList());

        ResponseEntity<Object> response = statsClient.readStatEvent(start.format(FORMATTER),
                LocalDateTime.now().format(FORMATTER), uris, true);

        try {
            HitResponseDto[] stats = objectMapper.readValue(
                    objectMapper.writeValueAsString(response.getBody()), HitResponseDto[].class);



            for (HitResponseDto stat : stats) {
                view.put(
                        Long.parseLong(stat.getUri().replaceAll("\\D+", "")),
                        stat.getHits());
            }

        } catch (JsonProcessingException e) {
            throw new StatisticParsingException("Ошибка получения данных статистики");
        }
        return view;
    }

    @Transactional
    @Override
    public void addHits(HttpServletRequest request) {
        statsClient.addStatEvent(HitDto.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .app(app)
                .build());
        log.info("Отправлен запрос на сохранение статистики");
    }
}
