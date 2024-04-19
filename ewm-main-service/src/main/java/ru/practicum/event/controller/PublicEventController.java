package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.event.service.impl.PublicEventServiceImpl;
import ru.practicum.event.service.realization.PublicEventService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
@Slf4j
public class PublicEventController {
    private final PublicEventServiceImpl PublicEventService;
}
