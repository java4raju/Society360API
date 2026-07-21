package com.society360.auth.service;

import com.society360.auth.dto.AuthResponse;
import com.society360.auth.dto.LoginRequest;
import com.society360.auth.entity.User;
import com.society360.auth.repository.UserRepository;
import com.society360.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));
        return AuthResponse.of(
            jwtService.generateAccessToken(user),
            jwtService.generateRefreshToken(user),
            user
        );
    }

    public AuthResponse refresh(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", email));
        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        return AuthResponse.of(jwtService.generateAccessToken(user), refreshToken, user);
    }
}
