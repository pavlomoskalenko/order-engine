package click.pavlomoskalenko.ordersystem.dto;

import click.pavlomoskalenko.ordersystem.model.Order;
import click.pavlomoskalenko.ordersystem.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Product sellProduct;
    private Product buyProduct;
    private UserResponse owner;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.sellProduct = order.getSellProduct();
        this.buyProduct = order.getBuyProduct();
        this.owner = new UserResponse(order.getOwner());
    }
}
