package click.pavlomoskalenko.orderengine.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public interface JwtService {
    String generateAccessToken(String email, List<String> roles);

    String generateRefreshToken(String email);

    String extractUsername(String token);

    Claims extractClaims(String token);

    Collection<? extends GrantedAuthority> extractRoles(String token);

    boolean hasTokenExpired(String token);
}
