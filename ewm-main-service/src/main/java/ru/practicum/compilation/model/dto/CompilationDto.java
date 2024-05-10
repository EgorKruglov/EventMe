package ru.practicum.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;
import ru.practicum.event.model.dto.EventDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    private Boolean pinned;

    @NotBlank(groups = {Validator.Create.class, Validator.Update.class})
    @Size(min = 1, max = 255, groups = {Validator.Create.class, Validator.Update.class})
    private String title;

    private List<EventDto> events;
}
