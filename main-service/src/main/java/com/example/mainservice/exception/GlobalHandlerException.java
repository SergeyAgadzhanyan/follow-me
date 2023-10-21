package com.example.mainservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalHandlerException {
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public NotFoundException handleMethodArgumentNotValidException(NotFoundException ex) {
        log.warn("Получен статус 404 Not found {}", ex.getMessage(), ex);
        return new NotFoundException("Not found");
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BadRequestException handleBadRequestException(RuntimeException ex) {
        log.warn("Получен статус 400 Bad Request {}", ex.getMessage(), ex);
        return new BadRequestException(ex.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ConflictException handleConflictException(ConflictException ex) {
        log.warn("Получен статус 409 Conflict {}", ex.getMessage(), ex);
        return new ConflictException(ex.getMessage());

    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Throwable handleThrowable(Throwable ex) {
        log.warn("Получен статус 500 Internal Server Error {}", ex.getMessage(), ex);
        return new Throwable("Internal_Server_Error");
    }


}
