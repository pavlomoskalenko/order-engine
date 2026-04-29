package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dao.OrderRepository;
import click.pavlomoskalenko.ordersystem.dao.ProductRepository;
import click.pavlomoskalenko.ordersystem.dao.UserRepository;
import click.pavlomoskalenko.ordersystem.dto.OrderRequest;
import click.pavlomoskalenko.ordersystem.dto.OrderResponse;
import click.pavlomoskalenko.ordersystem.exception.OrderNotFoundException;
import click.pavlomoskalenko.ordersystem.exception.ProductNotFoundException;
import click.pavlomoskalenko.ordersystem.exception.UserNotFoundException;
import click.pavlomoskalenko.ordersystem.model.Order;
import click.pavlomoskalenko.ordersystem.model.Product;
import click.pavlomoskalenko.ordersystem.model.User;
import click.pavlomoskalenko.ordersystem.orderbook.OrderBook;
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
    private final UserRepository userRepository;
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
        if (!userEmail.equals(order.getOwner().getEmail())) {
            throw new OrderNotFoundException("Order not found");
        }
        return new OrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest, String userEmail) {
        User owner = userRepository.findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist"));
        Product sellProduct = productRepository.findById(orderRequest.getSellProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with such id doesn't exist"));
        Product buyProduct = productRepository.findById(orderRequest.getBuyProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product with such id doesn't exist"));

        Order order = orderRepository.save(
                new Order(sellProduct, orderRequest.getSellAmount(), buyProduct, orderRequest.getBuyAmount(), owner));

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
        if (userEmail.equals(order.getOwner().getEmail())) {
            order.setStatus(Order.OrderStatus.CANCELED);
        }

        return new OrderResponse(order);
    }

}
