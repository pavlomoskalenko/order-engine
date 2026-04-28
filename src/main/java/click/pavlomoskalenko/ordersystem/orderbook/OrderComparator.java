package click.pavlomoskalenko.ordersystem.orderbook;

import click.pavlomoskalenko.ordersystem.model.Order;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        int sellAmount = Integer.compare(o1.getSellAmount(), o2.getSellAmount());

        if (sellAmount == 0) {
            int buyAmount = Integer.compare(o1.getBuyAmount(), o2.getBuyAmount());

            if (buyAmount == 0) {
                return Long.compare(o1.getId(), o2.getId());
            } else {
                return buyAmount > 0 ? -1 : 1;
            }
        } else {
            return sellAmount > 0 ? 1 : -1;
        }
    }
}
