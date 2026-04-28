package click.pavlomoskalenko.ordersystem.orderbook;

import click.pavlomoskalenko.ordersystem.model.Order;

public interface OrderBook {
    void placeOrder(Order order);
}
