package ru.practicum.compilation.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class NewCompilationDto {
    private Boolean pinned;

    @NotBlank(groups = {Validator.Create.class, Validator.Update.class})
    @Size(min = 10, max = 255, groups = {Validator.Create.class, Validator.Update.class})
    private String title;

    private List<Long> events;
}
