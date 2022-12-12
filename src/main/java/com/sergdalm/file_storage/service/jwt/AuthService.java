package com.sergdalm.file_storage.service.jwt;

import com.sergdalm.file_storage.dto.UserLoginDto;
import com.sergdalm.file_storage.dto.jwt.JwtAuthentication;
import com.sergdalm.file_storage.dto.jwt.JwtRequest;
import com.sergdalm.file_storage.dto.jwt.JwtResponse;
import com.sergdalm.file_storage.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final Map<String, String> refreshStorage = new HashMap<>();
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @SneakyThrows
    public JwtResponse login(@NonNull JwtRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));
        final UserLoginDto user = (UserLoginDto) authentication.getPrincipal();
        if (user.getUsername() != null) {
            final String accessToken = jwtProvider.generateAccessToken(user);
            final String refreshToken = jwtProvider.generateRefreshToken(user);
            refreshStorage.put(user.getUsername(), refreshToken);
            return new JwtResponse(accessToken, refreshToken);
        } else {
            throw new AuthException("Wrong password");
        }
    }

    public JwtResponse setToken(UserLoginDto user) {
        final String accessToken = jwtProvider.generateAccessToken(user);
        final String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshStorage.put(user.getUsername(), refreshToken);

        return new JwtResponse(accessToken, refreshToken);
    }

    @SneakyThrows
    public JwtResponse getAccessToken(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserLoginDto user = userService.loadUserByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                return new JwtResponse(accessToken, null);
            }
        }
        return new JwtResponse(null, null);
    }

    @SneakyThrows
    public JwtResponse refresh(@NonNull String refreshToken) {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            final Claims claims = jwtProvider.getRefreshClaims(refreshToken);
            final String login = claims.getSubject();
            final String saveRefreshToken = refreshStorage.get(login);
            if (saveRefreshToken != null && saveRefreshToken.equals(refreshToken)) {
                final UserLoginDto user = userService.loadUserByUsername(login);
                final String accessToken = jwtProvider.generateAccessToken(user);
                final String newRefreshToken = jwtProvider.generateRefreshToken(user);
                refreshStorage.put(user.getUsername(), newRefreshToken);
                return new JwtResponse(accessToken, newRefreshToken);
            }
        }
        throw new AuthException("Invalid JWT token");
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}
