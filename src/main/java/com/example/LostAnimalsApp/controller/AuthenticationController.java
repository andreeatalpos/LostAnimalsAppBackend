package com.example.LostAnimalsApp.controller;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.LoginDTO;
import com.example.LostAnimalsApp.service.implementation.AuthenticationServiceImpl;
import com.example.LostAnimalsApp.util.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthenticationController {

    private final AuthenticationServiceImpl authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthDTO authDTO) {
        try {
            AuthenticationResponse response = authService.register(authDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            AuthenticationResponse response = authService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }
}

