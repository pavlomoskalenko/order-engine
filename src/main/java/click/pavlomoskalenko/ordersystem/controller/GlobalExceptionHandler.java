package click.pavlomoskalenko.ordersystem.controller;

import click.pavlomoskalenko.ordersystem.dto.ErrorResponse;
import click.pavlomoskalenko.ordersystem.exception.JwtTokenException;
import click.pavlomoskalenko.ordersystem.exception.OrderNotFoundException;
import click.pavlomoskalenko.ordersystem.exception.ProductNotFoundException;
import click.pavlomoskalenko.ordersystem.exception.UserAlreadyExistsException;
import click.pavlomoskalenko.ordersystem.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getFieldErrors().stream()
                .filter((error) -> error.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing));

        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Invalid request content")
                .path(httpServletRequest.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleConflictExceptions(UserAlreadyExistsException ex) {
        return buildErrorResponse(CONFLICT, ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(UNAUTHORIZED)
    public ErrorResponse handleAuthExceptions() {
        return buildErrorResponse(UNAUTHORIZED, "Invalid username or password");
    }

    @ExceptionHandler(JwtTokenException.class)
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse handleJwtExceptions(JwtTokenException ex) {
        return buildErrorResponse(BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({ProductNotFoundException.class, OrderNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(RuntimeException ex) {
        return buildErrorResponse(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected() {
        return buildErrorResponse(INTERNAL_SERVER_ERROR, "An unexpected error occurred");
    }

    private ErrorResponse buildErrorResponse(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(httpServletRequest.getRequestURI())
                .build();
    }
}
