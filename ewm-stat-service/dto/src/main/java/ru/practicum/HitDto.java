package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HitDto {
    private Long id;

    @NotBlank(groups = Validator.Create.class)
    @Size(max = 255, groups = Validator.Create.class)
    private String app;

    @NotBlank(groups = Validator.Create.class)
    @Size(max = 255, groups = Validator.Create.class)
    private String uri;

    @NotBlank(groups = Validator.Create.class)
    @Size(max = 255, groups = Validator.Create.class)
    private String ip;

    @NotNull(groups = Validator.Create.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp;
}
