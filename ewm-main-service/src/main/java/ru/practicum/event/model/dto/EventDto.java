package ru.practicum.event.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
    private Long id;

    private String annotation;

    private Category category;

    private Long confirmedRequests;

    private LocalDateTime eventDate;

    private User initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
