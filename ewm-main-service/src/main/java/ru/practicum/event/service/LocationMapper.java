package ru.practicum.event.service;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.LocationDto;

@UtilityClass
public class LocationMapper {
    public Location toLocation(LocationDto locationDto) {
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }

    public LocationDto toDto(Location location) {
        return LocationDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }
}
