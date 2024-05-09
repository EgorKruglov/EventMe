package ru.practicum.event.service;

import lombok.experimental.UtilityClass;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.dto.EventDto;
import ru.practicum.event.model.dto.PrivateEventRequestDto;
import ru.practicum.event.model.dto.TotalEventDto;
import ru.practicum.user.service.UserMapper;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TotalEventDto toTotalDto(Event event) {
        TotalEventDto totalEvent = TotalEventDto.builder()
                .id(event.getId())
                .createdOn(event.getCreatedOn().format(FORMATTER))
                .initiator(UserMapper.toUserDto(event.getInitiator()))
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getView())
                .state(event.getState())
                .annotation(event.getAnnotation())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .paid(event.getPaid())
                .title(event.getTitle())
                .category(CategoryMapper.toDto(event.getCategory()))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .location(LocationMapper.toDto(event.getLocation()))
                .build();

        if (event.getPublishedOn() != null) {
            totalEvent.setPublishedOn(event.getPublishedOn().format(FORMATTER));
        }

        return totalEvent;
    }

    public static EventDto toDto(Event event, Long view, Long confirmedRequests) {
        return EventDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .confirmedRequests(confirmedRequests)
                .views(view)
                .paid(event.getPaid())
                .annotation(event.getAnnotation())
                .initiator(event.getInitiator())
                .category(event.getCategory())
                .annotation(event.getAnnotation())
                .build();
    }

    public static EventDto toDto(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .eventDate(event.getEventDate())
                .title(event.getTitle())
                .confirmedRequests(event.getConfirmedRequests())
                .views(event.getView())
                .paid(event.getPaid())
                .annotation(event.getAnnotation())
                .initiator(event.getInitiator())
                .category(event.getCategory())
                .annotation(event.getAnnotation())
                .build();
    }

    public static List<EventDto> eventListToDtoList(List<Event> events) {
        return events.stream().map(EventMapper::toDto).collect(Collectors.toList());
    }

    public List<TotalEventDto> eventListToTotalDtoList(List<Event> list) {
        return list.stream().map(EventMapper::toTotalDto).collect(Collectors.toList());
    }

    public static Event privatetoEvent(PrivateEventRequestDto eventDto) {
        return Event.builder()
                .eventDate(eventDto.getEventDate())
                .annotation(eventDto.getAnnotation())
                .category(Category.builder().id(eventDto.getCategory()).build())
                .paid(eventDto.isPaid())
                .description(eventDto.getDescription())
                .title(eventDto.getTitle())
                .participantLimit(eventDto.getParticipantLimit())
                .requestModeration(eventDto.isRequestModeration())
                .location(LocationMapper.toLocation(eventDto.getLocation()))
                .build();
    }
}
