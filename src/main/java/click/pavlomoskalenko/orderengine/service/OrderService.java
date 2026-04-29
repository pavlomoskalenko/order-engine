package click.pavlomoskalenko.orderengine.service;

import click.pavlomoskalenko.orderengine.dto.OrderRequest;
import click.pavlomoskalenko.orderengine.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> findAll(String userEmail);
    OrderResponse findById(Long orderId, String userEmail);
    OrderResponse placeOrder(OrderRequest orderRequest, String userEmail);
    OrderResponse cancelOrder(Long orderId, String userEmail);
}
