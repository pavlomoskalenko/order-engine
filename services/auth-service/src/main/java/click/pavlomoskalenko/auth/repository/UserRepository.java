package click.pavlomoskalenko.auth.repository;

import click.pavlomoskalenko.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
