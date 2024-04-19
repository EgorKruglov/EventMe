package ru.practicum.category.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    private Long id;

    @NotBlank(groups = {Validator.Create.class, Validator.Update.class})
    @Size(min = 5, max = 255, groups = {Validator.Create.class, Validator.Update.class})
    private String name;
}
