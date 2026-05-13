package click.pavlomoskalenko.order.api.rest.controller;

import click.pavlomoskalenko.order.api.rest.dto.ErrorResponse;
import click.pavlomoskalenko.order.exception.InvalidOrderStateException;
import click.pavlomoskalenko.order.exception.OrderNotFoundException;
import click.pavlomoskalenko.order.exception.ProductNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream()
                .filter(error -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing));

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid request content")
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler({OrderNotFoundException.class, ProductNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildErrorResponse(NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleInvalidState(InvalidOrderStateException ex, HttpServletRequest request) {
        return buildErrorResponse(CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(HttpServletRequest request) {
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "An unexpected error occurred", request);
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message, HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
    }
}
