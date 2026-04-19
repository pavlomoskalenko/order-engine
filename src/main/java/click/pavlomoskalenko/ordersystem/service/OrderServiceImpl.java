package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dao.OrderRepository;
import click.pavlomoskalenko.ordersystem.dao.UserRepository;
import click.pavlomoskalenko.ordersystem.dto.OrderRequest;
import click.pavlomoskalenko.ordersystem.dto.OrderResponse;
import click.pavlomoskalenko.ordersystem.exception.ProductNotFoundException;
import click.pavlomoskalenko.ordersystem.exception.UserNotFoundException;
import click.pavlomoskalenko.ordersystem.model.Order;
import click.pavlomoskalenko.ordersystem.model.Product;
import click.pavlomoskalenko.ordersystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll().stream().map(OrderResponse::new).toList();
    }

    @Override
    public OrderResponse findById(Long orderId) {
        return orderRepository.findById(orderId).map(OrderResponse::new).orElse(null);
    }

    @Override
    public OrderResponse placeOrder(OrderRequest orderRequest, String userEmail) {
        User owner = userRepository
                .findUserByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist"));
        Product sellProduct =
                productService.findProduct(orderRequest.getSellProduct().getName());
        Product buyProduct =
                productService.findProduct(orderRequest.getBuyProduct().getName());
        if (sellProduct == null || buyProduct == null) {
            throw new ProductNotFoundException("Product with such name doesn't exist");
        }

        Order order = new Order(sellProduct, buyProduct, owner);
        return new OrderResponse(orderRepository.save(order));
    }

    @Override
    public void deleteOrder(Long orderId, String userEmail) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            User owner = order.get().getOwner();
            if (userEmail.equals(owner.getEmail())) {
                orderRepository.deleteById(orderId);
            }
        }
    }
}
