package click.pavlomoskalenko.ordersystem.controller;

import click.pavlomoskalenko.ordersystem.dto.*;
import click.pavlomoskalenko.ordersystem.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

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

}
