package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dto.OrderRequest;
import click.pavlomoskalenko.ordersystem.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> findAll();

    OrderResponse findById(Long orderId);

    OrderResponse placeOrder(OrderRequest orderRequest, String userEmail);

    void deleteOrder(Long orderId, String userEmail);
}
