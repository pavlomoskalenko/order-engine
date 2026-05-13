package click.pavlomoskalenko.order.api.rest.controller;

import click.pavlomoskalenko.order.api.rest.dto.OrderRequest;
import click.pavlomoskalenko.order.api.rest.dto.OrderResponse;
import click.pavlomoskalenko.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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
}
