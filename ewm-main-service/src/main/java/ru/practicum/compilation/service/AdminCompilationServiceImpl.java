package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        log.info("Запрос на добавление подборки мероприятий");
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null && newCompilationDto.getEvents().size() != 0) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }

        if (newCompilationDto.getPinned() == null) {
            newCompilationDto.setPinned(false);
        }
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), events);
    }

    @Override
    public void deleteCompilation(Long id) {
        log.info("Запрос на удаление подборки мероприятий");
        compilationRepository.deleteById(id);
    }

    @Override
    public CompilationDto updateCompilation(Long id, NewCompilationDto newCompilationDto) {
        log.info("Запрос на обновление подборки мероприятий {}", newCompilationDto);
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Подборка мероприятий не найдена"));

        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), compilation.getEvents());
    }
}
