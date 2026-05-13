package click.pavlomoskalenko.order.orderbook;

import click.pavlomoskalenko.order.model.Order;

public interface OrderBook {
    void placeOrder(Order order);
}
