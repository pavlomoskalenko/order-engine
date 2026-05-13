package click.pavlomoskalenko.order.orderbook;

import click.pavlomoskalenko.order.model.Order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        int sellAmount = o1.getSellAmount().compareTo(o2.getSellAmount());
        if (sellAmount != 0) {
            return sellAmount > 0 ? 1 : -1;
        }

        int buyAmount = o1.getBuyAmount().compareTo(o2.getBuyAmount());
        if (buyAmount != 0) {
            return buyAmount > 0 ? -1 : 1;
        }

        return Long.compare(o1.getId(), o2.getId());
    }
}
