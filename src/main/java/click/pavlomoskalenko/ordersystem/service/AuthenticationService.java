package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dto.*;
import jakarta.validation.Valid;

public interface AuthenticationService {
    UserResponse register(RegistrationRequest registrationRequest);
    TokenResponse login(@Valid LoginRequest loginRequest);
    TokenResponse refresh(RefreshRequest refreshRequest);
}
