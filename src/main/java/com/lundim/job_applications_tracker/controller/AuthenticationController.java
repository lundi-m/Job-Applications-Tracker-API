package com.lundim.job_applications_tracker.controller;

import com.lundim.job_applications_tracker.dto.auth.AuthResponse;
import com.lundim.job_applications_tracker.dto.auth.LoginRequest;
import com.lundim.job_applications_tracker.dto.auth.RefreshRequest;
import com.lundim.job_applications_tracker.dto.auth.RegisterRequest;
import com.lundim.job_applications_tracker.dto.user.UserResponse;
import com.lundim.job_applications_tracker.service.AuthenticationService;
import com.lundim.job_applications_tracker.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job-applications/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserResponse response = authService.registerUser(request);
        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @Valid @RequestBody RefreshRequest request){
        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody RefreshRequest request){
                 authService.logout(request);
        return ResponseEntity.ok("Logged out successfully");
    }
}
