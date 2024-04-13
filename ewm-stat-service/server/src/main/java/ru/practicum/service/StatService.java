package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {
    HitDto createHit(HitDto hitDto);

    List<HitResponseDto> readStat(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
