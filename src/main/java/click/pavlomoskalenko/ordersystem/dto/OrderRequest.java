package click.pavlomoskalenko.ordersystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "Sell product is required")
    private ProductRequest sellProduct;

    @NotNull(message = "Buy product is required")
    private ProductRequest buyProduct;
}
