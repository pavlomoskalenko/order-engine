package click.pavlomoskalenko.auth.api.controller;

import click.pavlomoskalenko.auth.api.dto.*;
import click.pavlomoskalenko.auth.service.AuthenticationService;
import io.jsonwebtoken.security.Jwk;
import io.jsonwebtoken.security.Jwks;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;
    private final Jwk<?> jwtPublicJwk;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        return authService.register(registrationRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public TokenResponse refresh(@RequestBody @Valid RefreshRequest refreshRequest) {
        return authService.refresh(refreshRequest);
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        return Jwks.set()
                .add(jwtPublicJwk)
                .build();
    }
}
