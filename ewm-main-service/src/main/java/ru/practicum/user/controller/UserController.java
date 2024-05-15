package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.Validator;
import ru.practicum.user.model.dto.TotalUserDto;
import ru.practicum.user.service.UserService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<TotalUserDto> addUser(@RequestBody @Validated({Validator.Create.class}) TotalUserDto userDto) {
        TotalUserDto result = userService.addUser(userDto);
        log.info("Добавлен пользователь {}", result);
        return ResponseEntity.status((HttpStatus.CREATED)).body(result);
    }

    @GetMapping
    public ResponseEntity<List<TotalUserDto>> getUsers(@RequestParam(defaultValue = "") List<Long> ids,
                                                       @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                       @RequestParam(defaultValue = "10") @Positive int size) {
        List<TotalUserDto> result = userService.getUsers(ids, from, size);
        log.info("Отправлен список пользователей по запросу");
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Удалён пользователь id:{}", userId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("User deleted: " + userId);
    }
}
