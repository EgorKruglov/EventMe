package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;
import ru.practicum.category.model.dto.CategoryDto;
import ru.practicum.event.status.State;
import ru.practicum.user.model.dto.UserDto;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TotalEventDto {
    private Long id;

    @Size(min = 1, max = 2000, groups = Validator.Update.class)
    private String annotation;

    private CategoryDto category;

    private String createdOn;

    @Size(min = 1, max = 5000, groups = Validator.Update.class)
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private String eventDate;

    private UserDto initiator;

    private LocationDto location;

    private Boolean paid;

    @PositiveOrZero(groups = Validator.Update.class)
    private Integer participantLimit;

    private Boolean requestModeration;

    private State state;

    @Size(min = 1, max = 255, groups = Validator.Update.class)
    private String title;

    private Long views;

    private Long confirmedRequests;

    private String publishedOn;
}
