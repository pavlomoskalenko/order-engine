package click.pavlomoskalenko.order.service;

import click.pavlomoskalenko.order.api.rest.dto.OrderRequest;
import click.pavlomoskalenko.order.api.rest.dto.OrderResponse;
import click.pavlomoskalenko.order.exception.InvalidOrderStateException;
import click.pavlomoskalenko.order.exception.OrderNotFoundException;
import click.pavlomoskalenko.order.exception.ProductNotFoundException;
import click.pavlomoskalenko.order.model.Order;
import click.pavlomoskalenko.order.model.Product;
import click.pavlomoskalenko.order.orderbook.OrderBook;
import click.pavlomoskalenko.order.repository.OrderRepository;
import click.pavlomoskalenko.order.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderBook orderBook;

    @Override
    @Transactional
    public List<OrderResponse> findAll(String userEmail) {
        return orderRepository.findAllByOwnerEmail(userEmail).stream()
                .map(OrderResponse::new)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse findById(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!userEmail.equals(order.getOwnerEmail())) {
            throw new OrderNotFoundException("Order not found");
        }
        return new OrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest, String userEmail) {
        Product sellProduct = productRepository.findById(orderRequest.getSellProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with such id doesn't exist"));
        Product buyProduct = productRepository.findById(orderRequest.getBuyProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with such id doesn't exist"));

        Order order = orderRepository.save(new Order(sellProduct, orderRequest.getSellAmount(), buyProduct,
                        orderRequest.getBuyAmount(), userEmail));

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                orderBook.placeOrder(order);
            }
        });

        return new OrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, String userEmail) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (!userEmail.equals(order.getOwnerEmail())) {
            throw new OrderNotFoundException("Order not found");
        }
        if (order.getStatus() != Order.OrderStatus.NEW) {
            throw new InvalidOrderStateException("Only NEW orders can be canceled");
        }
        order.setStatus(Order.OrderStatus.CANCELED);

        return new OrderResponse(order);
    }

}
