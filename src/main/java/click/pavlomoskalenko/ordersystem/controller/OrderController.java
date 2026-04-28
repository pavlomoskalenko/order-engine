package click.pavlomoskalenko.ordersystem.controller;

import click.pavlomoskalenko.ordersystem.dto.ErrorResponse;
import click.pavlomoskalenko.ordersystem.dto.OrderRequest;
import click.pavlomoskalenko.ordersystem.dto.OrderResponse;
import click.pavlomoskalenko.ordersystem.exception.UserNotFoundException;
import click.pavlomoskalenko.ordersystem.service.OrderService;
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
        String userEmail = token.getName();
        return orderService.findAll(userEmail);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId, JwtAuthenticationToken token) {
        String userEmail = token.getName();
        return orderService.findById(orderId, userEmail);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestBody @Valid OrderRequest orderRequest, JwtAuthenticationToken token) {
        String userEmail = token.getName();
        return orderService.placeOrder(orderRequest, userEmail);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOrder(@PathVariable Long orderId, JwtAuthenticationToken token) {
        String userEmail = token.getName();
        orderService.deleteOrder(orderId, userEmail);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleAuthExceptions(UserNotFoundException ex, HttpServletRequest req) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(req.getRequestURI())
                .build();
    }
}
