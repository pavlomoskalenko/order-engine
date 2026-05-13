package click.pavlomoskalenko.order.api.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "Sell product is required")
    private Long sellProductId;

    @NotNull(message = "Sell amount is required")
    @Positive(message = "Sell amount must be positive")
    private BigDecimal sellAmount;

    @NotNull(message = "Buy product is required")
    private Long buyProductId;

    @NotNull(message = "Buy amount is required")
    @Positive(message = "Buy amount must be positive")
    private BigDecimal buyAmount;

    @JsonIgnore
    @AssertTrue(message = "Sell and buy products must differ")
    public boolean isProductsDistinct() {
        return !sellProductId.equals(buyProductId);
    }
}
