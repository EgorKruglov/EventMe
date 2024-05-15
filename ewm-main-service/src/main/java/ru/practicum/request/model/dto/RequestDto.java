package ru.practicum.request.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.status.Status;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long requester;
    private Status status;
}
