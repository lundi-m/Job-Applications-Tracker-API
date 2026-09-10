package com.lundim.job_applications_tracker.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RefreshRequest {

    @NotNull(message = "Refresh token is required")
    private String refreshToken;
}
