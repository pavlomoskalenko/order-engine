package click.pavlomoskalenko.orderengine.orderbook;

import click.pavlomoskalenko.orderengine.model.Order;

public interface OrderBook {
    void placeOrder(Order order);
}
