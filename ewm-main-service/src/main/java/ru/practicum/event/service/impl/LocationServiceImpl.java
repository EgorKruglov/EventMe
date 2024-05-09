package ru.practicum.event.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Location;
import ru.practicum.event.repository.LocationRepository;
import ru.practicum.event.service.LocationService;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Transactional
    @Override
    public Location addLocation(Location location) {
        log.info("Сохранение локации мероприятия");
        return locationRepository.save(location);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Location> getLocation(Location location) {
        log.info("Получение локации мероприятия");
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon());
    }
}
