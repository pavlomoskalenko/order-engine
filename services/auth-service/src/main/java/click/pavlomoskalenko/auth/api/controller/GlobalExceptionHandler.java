package click.pavlomoskalenko.auth.api.controller;

import click.pavlomoskalenko.auth.api.dto.ErrorResponse;
import click.pavlomoskalenko.auth.exception.JwtTokenException;
import click.pavlomoskalenko.auth.exception.UserAlreadyExistsException;
import click.pavlomoskalenko.auth.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleConflict(UserAlreadyExistsException ex, HttpServletRequest request) {
        return buildErrorResponse(CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler({UserNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handleAuthFailure(HttpServletRequest request) {
        return buildErrorResponse(UNAUTHORIZED, "Invalid username or password", request);
    }

    @ExceptionHandler(JwtTokenException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleJwt(JwtTokenException ex, HttpServletRequest request) {
        return buildErrorResponse(BAD_REQUEST, ex.getMessage(), request);
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
