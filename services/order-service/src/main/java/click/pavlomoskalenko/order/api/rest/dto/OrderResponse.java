package click.pavlomoskalenko.order.api.rest.dto;

import click.pavlomoskalenko.order.model.Order;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrderResponse {
    private Long id;
    private String ownerEmail;
    private Long sellProductId;
    private BigDecimal sellAmount;
    private Long buyProductId;
    private BigDecimal buyAmount;
    private String status;
    private LocalDateTime createdAt;

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.ownerEmail = order.getOwnerEmail();
        this.sellProductId = order.getSellProduct().getId();
        this.sellAmount = order.getSellAmount();
        this.buyProductId = order.getBuyProduct().getId();
        this.buyAmount = order.getBuyAmount();
        this.status = order.getStatus().toString();
        this.createdAt = order.getCreatedAt();
    }
}
