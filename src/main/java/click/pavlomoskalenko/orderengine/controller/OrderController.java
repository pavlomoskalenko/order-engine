package click.pavlomoskalenko.orderengine.controller;

import click.pavlomoskalenko.orderengine.dto.ErrorResponse;
import click.pavlomoskalenko.orderengine.dto.OrderRequest;
import click.pavlomoskalenko.orderengine.dto.OrderResponse;
import click.pavlomoskalenko.orderengine.exception.UserNotFoundException;
import click.pavlomoskalenko.orderengine.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<OrderResponse> getAllOrders(JwtAuthenticationToken token) {
        return orderService.findAll(token.getName());
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId, JwtAuthenticationToken token) {
        return orderService.findById(orderId, token.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody @Valid OrderRequest orderRequest, JwtAuthenticationToken token) {
        return orderService.placeOrder(orderRequest, token.getName());
    }

    @PatchMapping("/{orderId}")
    public OrderResponse cancelOrder(@PathVariable Long orderId, JwtAuthenticationToken token) {
        return orderService.cancelOrder(orderId, token.getName());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex, HttpServletRequest req) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
    }
}
