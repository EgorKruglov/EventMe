package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.compilation.model.dto.CompilationDto;
import ru.practicum.compilation.model.dto.NewCompilationDto;
import ru.practicum.compilation.service.AdminCompilationService;

import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> addCompilation(@RequestBody @Validated(Validator.Create.class) NewCompilationDto compilationDto) {
        CompilationDto result = compilationService.addCompilation(compilationDto);
        log.info("Добавлена новая подборка мероприятий {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCompilation(@PathVariable @Positive Long id) {
        compilationService.deleteCompilation(id);
        log.info("Удалена подборка мероприятий id:{}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Удалена подборка мероприятий id:" + id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long id,
                                                      @RequestBody @Validated(Validator.Update.class) NewCompilationDto compilationDto) {
        CompilationDto result = compilationService.updateCompilation(id, compilationDto);
        log.info("Обновлена подборка мероприятий {}", result);
        return ResponseEntity.ok().body(result);
    }
}
