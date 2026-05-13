package click.pavlomoskalenko.order.repository;

import click.pavlomoskalenko.order.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByNameIgnoreCase(String name);
}
