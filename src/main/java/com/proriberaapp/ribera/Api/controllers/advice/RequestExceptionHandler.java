package com.proriberaapp.ribera.Api.controllers.advice;
import com.proriberaapp.ribera.Api.controllers.exception.RequestException;
import com.proriberaapp.ribera.Domain.dto.response.ErrorFieldResponse;
import com.proriberaapp.ribera.Domain.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class RequestExceptionHandler {

    @ExceptionHandler(RequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleRequestException(RequestException ex) {
        ErrorResponse error = new ErrorResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.name(),
                System.currentTimeMillis()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ErrorFieldResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorFieldResponse error = ErrorFieldResponse.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .fieldErrors(fieldErrors)
                .build();

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error));
    }
}
