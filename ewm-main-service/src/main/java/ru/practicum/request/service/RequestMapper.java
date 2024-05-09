package ru.practicum.request.service;

import lombok.experimental.UtilityClass;
import ru.practicum.request.model.Request;
import ru.practicum.request.model.dto.RequestDto;
import ru.practicum.request.model.dto.RequestShortDto;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static List<RequestDto> listToDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList());
    }

    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .status(request.getStatus())
                .build();
    }

    public static RequestShortDto toRequestShort(RequestShortDto requestDto) {
        return RequestShortDto.builder()
                .requestIds(requestDto.getRequestIds())
                .status(requestDto.getStatus())
                .build();
    }
}
