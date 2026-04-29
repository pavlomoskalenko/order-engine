package click.pavlomoskalenko.orderengine.service;

import click.pavlomoskalenko.orderengine.dto.*;
import jakarta.validation.Valid;

public interface AuthenticationService {
    UserResponse register(RegistrationRequest registrationRequest);
    TokenResponse login(@Valid LoginRequest loginRequest);
    TokenResponse refresh(RefreshRequest refreshRequest);
}
