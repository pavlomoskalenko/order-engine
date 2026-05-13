package click.pavlomoskalenko.auth.service;

import click.pavlomoskalenko.auth.api.dto.*;
import click.pavlomoskalenko.auth.exception.JwtTokenException;
import click.pavlomoskalenko.auth.exception.UserAlreadyExistsException;
import click.pavlomoskalenko.auth.exception.UserNotFoundException;
//import click.pavlomoskalenko.auth.messaging.event.UserCreatedEvent;
//import click.pavlomoskalenko.auth.messaging.publisher.UserEventPublisher;
import click.pavlomoskalenko.auth.model.User;
import click.pavlomoskalenko.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
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
    //private final UserEventPublisher userEventPublisher;

    @Override
    @Transactional
    public UserResponse register(RegistrationRequest registrationRequest) {
        String email = registrationRequest.getEmail();
        if (userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException("User with such email already exists");

        String encodedPassword = passwordEncoder.encode(registrationRequest.getPassword());
        User savedUser = userRepository.save(new User(email, encodedPassword));

        //userEventPublisher.publishUserCreated(new UserCreatedEvent(savedUser));

        return new UserResponse(savedUser);
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
            throw new JwtTokenException("Refresh token has expired");
        }

        String username = jwtService.extractUsername(token);
        User user = userRepository
                .findUserByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User with such email doesn't exist"));

        String accessToken = jwtService.generateAccessToken(user.getEmail(), List.of("USER"));

        return new TokenResponse(accessToken, null);
    }
}
