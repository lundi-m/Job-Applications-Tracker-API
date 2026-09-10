package com.lundim.job_applications_tracker.controller;

import com.lundim.job_applications_tracker.dto.auth.AuthResponse;
import com.lundim.job_applications_tracker.dto.auth.LoginRequest;
import com.lundim.job_applications_tracker.dto.auth.RegisterRequest;
import com.lundim.job_applications_tracker.dto.user.UserResponse;
import com.lundim.job_applications_tracker.security.AuthenticationService;
import com.lundim.job_applications_tracker.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authenticationService.registerUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        UserDetails userDetails = authenticationService.authenticate(request.getEmail(), request.getPassword());

        String tokenValue = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(AuthResponse.builder()
                .token(tokenValue)
                .expiresIn(86400000L)
                .build());
    }
}
