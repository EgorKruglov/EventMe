package ru.practicum.comment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Validator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    @NotBlank
    @Size(min = 2, max = 2000, groups = {Validator.Create.class, Validator.Update.class})
    private String text;
}
