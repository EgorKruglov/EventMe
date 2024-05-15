package ru.practicum.compilation.service;

import ru.practicum.compilation.model.dto.CompilationDto;

import java.util.List;


public interface UserCompilationService {
    CompilationDto getCompilation(Long id);

    List<CompilationDto> getCompilations(Integer from, Integer size, Boolean pinned);
}
