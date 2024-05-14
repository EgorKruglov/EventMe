package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.exceptions.extraExceptions.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserCompilationServiceImpl implements UserCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto getCompilation(Long id) {
        log.info("Запрос на получение подборки мероприятий");
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Подборка мероприятий не найдена"));
        return CompilationMapper.toCompilationDto(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned) {
        log.info("Запрос на получение списка подборок мероприятий");
        Pageable pageable = PageRequest.of(from, size, Sort.Direction.ASC, "id");
        List<Compilation> compilations = (pinned == null) ? compilationRepository.findAll(pageable).getContent() :
                compilationRepository.findAllByPinned(pinned, pageable);
        return CompilationMapper.toDtoList(compilations);
    }
}
