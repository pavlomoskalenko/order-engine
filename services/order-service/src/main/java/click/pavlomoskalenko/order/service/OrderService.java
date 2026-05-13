package click.pavlomoskalenko.order.service;


import click.pavlomoskalenko.order.api.rest.dto.OrderRequest;
import click.pavlomoskalenko.order.api.rest.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    List<OrderResponse> findAll(String userEmail);
    OrderResponse findById(Long orderId, String userEmail);
    OrderResponse placeOrder(OrderRequest orderRequest, String userEmail);
    OrderResponse cancelOrder(Long orderId, String userEmail);
}
