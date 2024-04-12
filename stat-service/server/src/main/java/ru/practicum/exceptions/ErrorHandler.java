package ru.practicum.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.exceptions.extraExceptions.ValidationException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final Exception e) {
        log.debug(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }
}
