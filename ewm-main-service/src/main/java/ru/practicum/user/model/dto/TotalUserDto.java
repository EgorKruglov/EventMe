package ru.practicum.user.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.Validator;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class TotalUserDto {
    private Long id;

    @Email(groups = {Validator.Create.class})
    @Size(min = 5, max = 255, groups = {Validator.Create.class})
    @NotBlank(groups = {Validator.Create.class})
    private String email;

    @NotBlank(groups = {Validator.Create.class})
    @Size(min = 5, max = 255, groups = {Validator.Create.class})
    private String name;
}
