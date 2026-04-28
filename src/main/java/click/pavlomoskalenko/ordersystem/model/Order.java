package click.pavlomoskalenko.ordersystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "`order`")
@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sell_product_id", nullable = false)
    private final Product sellProduct;

    @NotNull
    @Column(name = "sell_product_amount", nullable = false)
    private final int sellAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_product_id", nullable = false)
    private final Product buyProduct;

    @NotNull
    @Column(name = "buy_product_amount", nullable = false)
    private final int buyAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private final User owner;

    @Setter
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public enum OrderStatus {
        NEW, RESOLVED
    }

}
