package click.pavlomoskalenko.ordersystem.dto;

import click.pavlomoskalenko.ordersystem.model.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {
    private Long id;
    private String name;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
    }
}
