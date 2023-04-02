package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.LoginDTO;
import com.example.LostAnimalsApp.util.AuthenticationResponse;
import lombok.RequiredArgsConstructor;

import com.example.LostAnimalsApp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(final AuthDTO user) {
        userService.createUser(user);
        var jwtToken = jwtService.generateToken(modelMapper.map(user, User.class));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse login(final LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        var user = modelMapper.map(userService.getUserByUsername(loginDTO.getUsername()), User.class);
        var jwtToken = jwtService.generateToken(modelMapper.map(user, User.class));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
