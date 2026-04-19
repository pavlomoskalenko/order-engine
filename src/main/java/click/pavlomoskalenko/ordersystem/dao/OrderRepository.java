package click.pavlomoskalenko.ordersystem.dao;

import click.pavlomoskalenko.ordersystem.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
