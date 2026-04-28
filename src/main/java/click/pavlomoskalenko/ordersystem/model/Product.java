package click.pavlomoskalenko.ordersystem.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "product")
@Immutable
@NoArgsConstructor(force = true)
@Data
@Setter(AccessLevel.NONE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    private final String name;
}
