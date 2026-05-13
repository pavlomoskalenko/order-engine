package click.pavlomoskalenko.order.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
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
    private final BigDecimal sellAmount;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buy_product_id", nullable = false)
    private final Product buyProduct;

    @NotNull
    @Column(name = "buy_product_amount", nullable = false)
    private final BigDecimal buyAmount;

    @NotNull
    @Column(name = "owner_email", nullable = false)
    private final String ownerEmail;

    @Setter
    @NotNull
    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.NEW;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public enum OrderStatus {
        NEW, RESOLVED, CANCELED
    }

}
