package click.pavlomoskalenko.auth.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;

@Configuration
public class JwtKeyConfig {

    @Bean
    public KeyPair jwtKeyPair() {
        return Jwts.SIG.RS512.keyPair().build();
    }

    @Bean
    public Jwk<?> jwtPublicJwk(KeyPair jwtKeyPair) {
        return Jwks.builder().key(jwtKeyPair.getPublic()).build();
    }
}
