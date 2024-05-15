package ru.practicum.event.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;
import ru.practicum.category.model.Category;
import ru.practicum.user.model.User;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private Long id;

    @Size(min = 20, max = 2000, groups = Validator.Update.class)
    private String annotation;

    private Category category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;

    private Long confirmedRequests;

    private User initiator;

    private Boolean paid;

    @Size(min = 3, max = 120, groups = Validator.Update.class)
    private String title;

    private Long views;
}
