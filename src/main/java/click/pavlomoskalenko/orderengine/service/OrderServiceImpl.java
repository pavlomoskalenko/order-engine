package click.pavlomoskalenko.orderengine.service;

import click.pavlomoskalenko.orderengine.dao.OrderRepository;
import click.pavlomoskalenko.orderengine.dao.ProductRepository;
import click.pavlomoskalenko.orderengine.dao.UserRepository;
import click.pavlomoskalenko.orderengine.dto.OrderRequest;
import click.pavlomoskalenko.orderengine.dto.OrderResponse;
import click.pavlomoskalenko.orderengine.exception.OrderNotFoundException;
import click.pavlomoskalenko.orderengine.exception.ProductNotFoundException;
import click.pavlomoskalenko.orderengine.exception.UserNotFoundException;
import click.pavlomoskalenko.orderengine.model.Order;
import click.pavlomoskalenko.orderengine.model.Product;
import click.pavlomoskalenko.orderengine.model.User;
import click.pavlomoskalenko.orderengine.orderbook.OrderBook;
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
