package ru.practicum.event.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;

    private String annotation;

    private Category category;

    private LocalDateTime eventDate;

    private Long confirmedRequests;

    private User initiator;

    private Boolean paid;

    private String title;

    private Long views;
}
