package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage notFoundException(NotFoundException exception) {
        log.info(exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage notValidException(MethodArgumentNotValidException exception) {
        log.info(exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage notValidException(AlreadyExistException exception) {
        log.info(exception.getMessage());
        return new ErrorMessage(exception.getMessage());
    }
}