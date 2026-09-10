package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.dto.auth.AuthResponse;
import com.lundim.job_applications_tracker.dto.auth.LoginRequest;
import com.lundim.job_applications_tracker.dto.auth.RefreshRequest;
import com.lundim.job_applications_tracker.dto.auth.RegisterRequest;
import com.lundim.job_applications_tracker.dto.user.UserResponse;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.entity.RefreshToken;
import com.lundim.job_applications_tracker.repository.UserRepository;
import com.lundim.job_applications_tracker.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public UserResponse registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists.");
        }

        CustomUser user = CustomUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .createdAt(Instant.now())
                .build();

        CustomUser saved = userRepository.save(user);

        return UserResponse.builder()
                .id(saved.getId())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .email(saved.getEmail())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public AuthResponse refresh(RefreshRequest request){
        RefreshToken refreshToken = refreshTokenService.verifyAndGet(request.getRefreshToken());
        String newAccessToken = jwtService.generateAccessToken(refreshToken.getUser().getEmail());

        return AuthResponse.builder()
                .refreshToken(refreshToken.getToken())
                .accessToken(newAccessToken)
                .build();
    }

    public void logout(RefreshRequest request){
        refreshTokenService.deleteByToken(request.getRefreshToken());
    }
}
