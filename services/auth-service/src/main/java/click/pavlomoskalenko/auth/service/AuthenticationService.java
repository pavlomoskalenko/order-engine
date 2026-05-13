package click.pavlomoskalenko.auth.service;

import click.pavlomoskalenko.auth.api.dto.*;

public interface AuthenticationService {
    UserResponse register(RegistrationRequest registrationRequest);
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse refresh(RefreshRequest refreshRequest);
}
