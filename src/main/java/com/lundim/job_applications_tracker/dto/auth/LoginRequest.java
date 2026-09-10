package com.lundim.job_applications_tracker.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull
    private String email;

    @NotNull
    private String password;
}
