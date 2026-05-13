package click.pavlomoskalenko.order.repository;

import click.pavlomoskalenko.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o JOIN FETCH o.buyProduct JOIN FETCH o.sellProduct WHERE o.ownerEmail = :email")
    List<Order> findAllByOwnerEmail(@Param("email") String email);

    @Query("SELECT o FROM Order o JOIN FETCH o.buyProduct JOIN FETCH o.sellProduct WHERE o.status = :status")
    List<Order> findAllByStatus(@Param("status") Order.OrderStatus status);
}
