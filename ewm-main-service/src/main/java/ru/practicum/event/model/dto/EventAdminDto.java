package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;
import ru.practicum.event.model.Location;
import ru.practicum.event.status.AdminEventStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EventAdminDto {
    @Size(min = 20, max = 2000, groups = Validator.Update.class)
    private String annotation;

    private Long category;

    @Size(min = 20, max = 7000, groups = Validator.Update.class)
    private String description;

    @Future(groups = Validator.Update.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    @PositiveOrZero(groups = Validator.Update.class)
    private Integer participantLimit;

    private Boolean requestModeration;

    private AdminEventStatus stateAction;

    @Size(min = 3, max = 120, groups = Validator.Update.class)
    private String title;
}
