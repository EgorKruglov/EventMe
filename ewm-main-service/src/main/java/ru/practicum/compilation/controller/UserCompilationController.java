package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.service.UserCompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
@Slf4j
public class UserCompilationController {
    private final UserCompilationService compilationService;

    @GetMapping("/{id}")
    public ResponseEntity<CompilationDto> getCompilation(@PathVariable Long id) {
        CompilationDto result = compilationService.getCompilation(id);
        log.info("Отправлена подборка мероприятий по запросу");
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                @RequestParam(defaultValue = "10") @Positive int size) {
        List<CompilationDto> result = compilationService.getCompilations(from, size, pinned);
        log.info("Отправлен список подборок мероприятий по запросу");
        return ResponseEntity.ok().body(result);
    }
}
