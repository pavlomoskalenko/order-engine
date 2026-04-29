package click.pavlomoskalenko.orderengine.orderbook;

import click.pavlomoskalenko.orderengine.dao.OrderRepository;
import click.pavlomoskalenko.orderengine.model.Order;
import click.pavlomoskalenko.orderengine.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Component
@RequiredArgsConstructor
public class OrderBookImpl implements OrderBook {

    private static final Logger logger = LoggerFactory.getLogger(OrderBookImpl.class);

    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    private final LinkedBlockingQueue<Order> orders = new LinkedBlockingQueue<>();
    private final Map<Pair<Product, Product>, Market> markets = new HashMap<>();

    @PostConstruct
    private void init() {
        Runnable consumer = () -> {
            while (true) {
                try {
                    Order order = orders.take();
                    processOrder(order);
                } catch (InterruptedException ex) {
                    logger.error("Order consumer thread is stopped.", ex);
                    break;
                } catch (Exception ex) {
                    logger.error("Error processing order, continuing.", ex);
                    // don't break — keep the thread alive
                }

            }
        };

        new Thread(consumer, "Thread-order-consumer").start();

        List<Order> newOrders = orderRepository.findAllByStatus(Order.OrderStatus.NEW);
        for (var order : newOrders) {
            placeOrder(order);
        }
    }

    @Override
    public void placeOrder(Order order) {
        try {
            orders.put(order);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.error("Couldn't put the order with id {} into the queue.\n{}", order.getId(), ex.getMessage());
        }
    }

    private void processOrder(Order order) {
        Product sellProduct = order.getSellProduct();
        Product buyProduct = order.getBuyProduct();

        Market market = markets.get(Pair.of(sellProduct, buyProduct));
        if (market == null) {
            market = new Market(sellProduct, buyProduct);
            markets.put(Pair.of(sellProduct, buyProduct), market);
            markets.put(Pair.of(buyProduct, sellProduct), market);
        }

        Optional<Order> matchedOrder = market.add(order);
        matchedOrder.ifPresent(matched -> {
            transactionTemplate.execute((status) -> {
                order.setStatus(Order.OrderStatus.RESOLVED);
                matched.setStatus(Order.OrderStatus.RESOLVED);

                orderRepository.save(order);
                orderRepository.save(matched);

                return null;
            });
        });
    }
}
