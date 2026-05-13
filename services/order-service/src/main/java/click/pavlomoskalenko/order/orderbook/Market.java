package click.pavlomoskalenko.order.orderbook;

import click.pavlomoskalenko.order.model.Order;
import click.pavlomoskalenko.order.model.Product;

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
            if (offer.getSellAmount().compareTo(order.getBuyAmount()) < 0) {
                break;
            }

            if (offer.getBuyAmount().compareTo(order.getSellAmount()) <= 0) {
                offersIterator.remove();
                return Optional.of(offer);
            }
        }

        orders.get(order.getSellProduct()).add(order);

        return Optional.empty();
    }

}
