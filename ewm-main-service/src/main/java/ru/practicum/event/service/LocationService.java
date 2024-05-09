package ru.practicum.event.service;

import ru.practicum.event.model.Location;
import ru.practicum.event.model.dto.LocationDto;

import java.util.Optional;

public interface LocationService {
    Location addLocation(Location location);

    Optional<Location> getLocation(Location location);
}
