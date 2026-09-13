package com.lundim.job_applications_tracker.testUtil;

import com.lundim.job_applications_tracker.dto.auth.AuthResponse;
import com.lundim.job_applications_tracker.dto.auth.LoginRequest;
import com.lundim.job_applications_tracker.dto.auth.RefreshRequest;
import com.lundim.job_applications_tracker.dto.auth.RegisterRequest;
import com.lundim.job_applications_tracker.dto.user.UserResponse;

public class AuthData {

    public static RegisterRequest createRegisterRequest(){
        RegisterRequest request = new RegisterRequest();

        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setEmail("janedoe@gmail.com");
        request.setPassword("Found@Street213");

        return request;
    }

    public static UserResponse createUserResponse(){
        return UserResponse.builder()
                .id(1L)
                .firstName("Jane")
                .lastName("Doe")
                .email("janedoe@gmail.com")
                .build();
    }

    public static LoginRequest createLoginRequest(){
        LoginRequest request = new LoginRequest();
        request.setEmail("janedoe@gmail.com");
        request.setPassword("Missing@HELP123");

        return request;
    }

    public static AuthResponse createAuthResponse(){
        return AuthResponse.builder()
                .accessToken("JaneDoeAccessToken")
                .refreshToken("JaneDoeRefreshToken")
                .build();
    }

    public static RefreshRequest createRefreshRequest(){

        RefreshRequest request = new RefreshRequest();
        request.setRefreshToken("JaneDoeRefreshToken");

        return request;
    }
}

