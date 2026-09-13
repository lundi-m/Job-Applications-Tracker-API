package com.lundim.job_applications_tracker.controller;

import com.lundim.job_applications_tracker.dto.auth.LoginRequest;
import com.lundim.job_applications_tracker.dto.auth.RefreshRequest;
import com.lundim.job_applications_tracker.dto.auth.RegisterRequest;
import com.lundim.job_applications_tracker.exception.EmailAlreadyExistsException;
import com.lundim.job_applications_tracker.exception.InvalidTokenException;
import com.lundim.job_applications_tracker.security.JwtAuthenticationFilter;
import com.lundim.job_applications_tracker.service.AuthenticationService;
import com.lundim.job_applications_tracker.testUtil.AuthData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static com.lundim.job_applications_tracker.testUtil.AuthData.*;
import static com.lundim.job_applications_tracker.testUtil.AuthData.createRefreshRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @MockitoBean
    private AuthenticationService authService;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private ObjectMapper objectMapper;

    private String toJson(Object object){
        return objectMapper.writeValueAsString(object);
    }

    @Test
    void shouldRegisterUser() throws Exception{

        when(authService.registerUser(any(RegisterRequest.class)))
                .thenReturn(createUserResponse());

        mockMvc.perform(post("/job-applications/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRegisterRequest())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidRegisterRequest() throws Exception{

        RegisterRequest request = new RegisterRequest();

        mockMvc.perform(post("/job-applications/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnConflictWhenEmailAlreadyExists() throws Exception{

        when(authService.registerUser(any(RegisterRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("Email already exist"));

        mockMvc.perform(post("/job-applications/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRegisterRequest())))
                .andExpect(status().isConflict());
    }

    @Test
    void ShouldLoginSuccessfully() throws Exception{

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(AuthData.createAuthResponse());

        mockMvc.perform(post("/job-applications/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createLoginRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldReturnBadRequestForInvalidLoginRequest() throws Exception{

        LoginRequest request = new LoginRequest();

        mockMvc.perform(post("/job-applications/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnUnAuthorizedForBadCredentials() throws Exception{

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Invalid Credentials"));

        mockMvc.perform(post("/job-applications/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createLoginRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRefreshToken() throws Exception{

        when(authService.refresh(any(RefreshRequest.class)))
                .thenReturn(AuthData.createAuthResponse());

        mockMvc.perform(post("/job-applications/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRefreshRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenRefreshTokenIsInvalid() throws Exception{

        when(authService.refresh(any(RefreshRequest.class)))
                .thenThrow(new InvalidTokenException("Invalid Token"));

        mockMvc.perform(post("/job-applications/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRefreshRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLogoutSuccessfully() throws Exception{

        doNothing().when(authService).logout(AuthData.createRefreshRequest());

        mockMvc.perform(post("/job-applications/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(createRefreshRequest())))
                .andExpect(status().isOk());
    }
}
