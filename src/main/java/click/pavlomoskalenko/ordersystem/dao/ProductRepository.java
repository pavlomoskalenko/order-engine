package click.pavlomoskalenko.ordersystem.dao;

import click.pavlomoskalenko.ordersystem.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByNameIgnoreCase(String name);
}
