package ru.practicum.model;

import lombok.experimental.UtilityClass;
import ru.practicum.HitDto;

@UtilityClass
public class HitDtoMapper {
    public Hit toHit(HitDto hitDto) {
        return Hit.builder()
                .ip(hitDto.getIp())
                .uri(hitDto.getUri())
                .timestamp(hitDto.getTimestamp())
                .app(hitDto.getApp())
                .build();
    }

    public HitDto toHitDto(Hit hit) {
        return HitDto.builder()
                .timestamp(hit.getTimestamp())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .build();
    }
}
