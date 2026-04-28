package click.pavlomoskalenko.ordersystem.security;

import click.pavlomoskalenko.ordersystem.service.JwtService;
import click.pavlomoskalenko.ordersystem.service.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/register", "/api/refresh").permitAll()
                        .anyRequest().permitAll());
        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtService jwtService() {
        return new JwtServiceImpl();
    }

    @Bean
    public JwtDecoder jwtDecoder(JwtService jwtService) {
        return (token) -> {
            /* Test case with expired token, probably need to catch the exception and throw auth exception */
            Claims claims = jwtService.extractClaims(token);

            return new Jwt(
                    token,
                    claims.getIssuedAt().toInstant(),
                    claims.getExpiration().toInstant(),
                    Map.of("alg", "HS256"),
                    claims);
        };
    }
}
