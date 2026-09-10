package com.lundim.job_applications_tracker.service;

import com.lundim.job_applications_tracker.exception.InvalidTokenException;
import com.lundim.job_applications_tracker.model.entity.CustomUser;
import com.lundim.job_applications_tracker.model.entity.RefreshToken;
import com.lundim.job_applications_tracker.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecureRandom secureRandom;

    @Value("${jwt.refresh-token-expiration-ms}")
    private long refreshTokenExpirationMs;

    public RefreshToken createRefreshToken(CustomUser user) {
        byte[] randomBytes = new byte[64];
        secureRandom.nextBytes(randomBytes);
        String tokenValue = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(randomBytes);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(tokenValue);
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyAndGet(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()-> new InvalidTokenException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("Refresh token expired, please log in again");
        }

        return refreshToken;
    }

    @Transactional
    public void deleteByToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));

        refreshTokenRepository.deleteByToken(refreshToken.getToken());
    }
}
