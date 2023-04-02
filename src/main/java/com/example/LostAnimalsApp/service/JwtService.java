package com.example.LostAnimalsApp.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractUsername(final String token);

    <T> T extractClaim(final String token, Function<Claims, T> claimsResolver);

    String generateToken(final UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);
}

