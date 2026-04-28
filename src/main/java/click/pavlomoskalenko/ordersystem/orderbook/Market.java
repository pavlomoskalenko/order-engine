package click.pavlomoskalenko.ordersystem.orderbook;

import click.pavlomoskalenko.ordersystem.model.Order;
import click.pavlomoskalenko.ordersystem.model.Product;

import java.util.*;

public class Market {
    private final Map<Product, NavigableSet<Order>> orders = new HashMap<>();

    public Market(Product a, Product b) {
        orders.put(a, new TreeSet<>(new OrderComparator()));
        orders.put(b, new TreeSet<>(new OrderComparator()));
    }

    public Optional<Order> add(Order order) {
        NavigableSet<Order> offers = orders.get(order.getBuyProduct());
        Iterator<Order> offersIterator = offers.descendingIterator();
        while (offersIterator.hasNext()) {
            Order offer = offersIterator.next();
            if (offer.getSellAmount() < order.getBuyAmount()) {
                break;
            }

            if (offer.getBuyAmount() <= order.getSellAmount()) {
                offersIterator.remove();
                return Optional.of(offer);
            }
        }

        orders.get(order.getSellProduct()).add(order);

        return Optional.empty();
    }

}
