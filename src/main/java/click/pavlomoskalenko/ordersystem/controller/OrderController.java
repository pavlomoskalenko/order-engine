package click.pavlomoskalenko.ordersystem.controller;

import click.pavlomoskalenko.ordersystem.dto.OrderRequest;
import click.pavlomoskalenko.ordersystem.dto.OrderResponse;
import click.pavlomoskalenko.ordersystem.service.OrderService;
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
    public List<OrderResponse> getAllOrders() {
        return orderService.findAll();
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {
        return orderService.findById(orderId);
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
}
