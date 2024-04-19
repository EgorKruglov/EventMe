package ru.practicum.event.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.status.State;
import ru.practicum.user.model.dto.UserDto;

@Data
@Builder
public class TotalEventDto {
    private Long id;

    private String annotation;

    private CategoryDto category;

    private String createdOn;

    private String description;

    private String eventDate;

    private UserDto initiator;

    private LocationDto location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private State state;

    private String title;

    private Long views;

    private Long confirmedRequests;

    private String publishedOn;
}
