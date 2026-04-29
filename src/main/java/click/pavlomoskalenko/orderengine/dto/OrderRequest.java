package click.pavlomoskalenko.orderengine.dto;

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
    private Long sellProductId;

    @NotNull(message = "Sell amount is required")
    private Integer sellAmount;

    @NotNull(message = "Buy product is required")
    private Long buyProductId;

    @NotNull(message = "Buy amount is required")
    private Integer buyAmount;
}
