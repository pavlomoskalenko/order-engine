package click.pavlomoskalenko.ordersystem.service;

import click.pavlomoskalenko.ordersystem.dao.UserRepository;
import click.pavlomoskalenko.ordersystem.dto.*;
import click.pavlomoskalenko.ordersystem.exception.UserAlreadyExistsException;
import click.pavlomoskalenko.ordersystem.exception.UserNotFoundException;
import click.pavlomoskalenko.ordersystem.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse register(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        if (userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("User with such email already exists");

        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        User user = new User(email, encodedPassword);
        userRepository.save(user);

        return new UserResponse(user);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        User user = userRepository
                .findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("User password is incorrect");
        }

        String accessToken = jwtService.generateAccessToken(email, List.of("USER"));
        String refreshToken = jwtService.generateRefreshToken(email);

        return new TokenResponse(accessToken, refreshToken);
    }

    @Override
    public TokenResponse refresh(RefreshRequest refreshRequest) {
        String token = refreshRequest.getRefreshToken();

        if (jwtService.hasTokenExpired(token)) {
            throw new BadCredentialsException("Refresh token has expired");
        }

        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist"));

        String accessToken = jwtService.generateAccessToken(user.getEmail(), List.of("USER"));

        return new TokenResponse(accessToken, null);
    }
}
