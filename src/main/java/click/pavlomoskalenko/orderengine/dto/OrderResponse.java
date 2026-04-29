package click.pavlomoskalenko.orderengine.dto;

import click.pavlomoskalenko.orderengine.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private Long ownerId;
    private Long sellProductId;
    private int sellAmount;
    private Long buyProductId;
    private int buyAmount;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.ownerId = order.getOwner().getId();
        this.sellProductId = order.getSellProduct().getId();
        this.sellAmount = order.getSellAmount();
        this.buyProductId = order.getBuyProduct().getId();
        this.buyAmount = order.getBuyAmount();
        this.status = order.getStatus().toString();
        this.createdAt = order.getCreatedAt();
    }
}
