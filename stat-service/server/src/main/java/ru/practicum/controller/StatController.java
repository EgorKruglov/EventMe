package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.HitDto;
import ru.practicum.HitResponseDto;
import ru.practicum.service.StatService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class StatController {
    private final StatService statService;

    @PostMapping("/hit")
    public ResponseEntity<HitDto> addHit(@Valid @RequestBody HitDto hitDto) {
        HitDto hit = statService.createHit(hitDto);
        log.info("Сохранена статистика о запросе {}", hit);
        return ResponseEntity.status(HttpStatus.CREATED).body(hit);
    }

    @GetMapping("/hit")
    public String hello() {
        log.info("Получение привета");
        return "hello";
    }

    @GetMapping("/stats")
    public ResponseEntity<List<HitResponseDto>> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                        @RequestParam(defaultValue = "") List<String> uris,
                                                        @RequestParam(defaultValue = "false") boolean unique) {
        List<HitResponseDto> stat = statService.readStat(start, end, uris, unique);
        log.info("Статистика отправлена");
        return ResponseEntity.status(HttpStatus.OK).body(stat);
    }
}
