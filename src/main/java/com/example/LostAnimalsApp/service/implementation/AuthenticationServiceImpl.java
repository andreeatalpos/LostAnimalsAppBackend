package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.LoginDTO;
import com.example.LostAnimalsApp.service.AuthenticationService;
import com.example.LostAnimalsApp.util.AuthenticationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.example.LostAnimalsApp.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserServiceImpl userServiceImpl;
    private final JwtServiceImpl jwtServiceImpl;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(final AuthDTO user) {
        userServiceImpl.createUser(user);
        var jwtToken = jwtServiceImpl.generateToken(modelMapper.map(user, User.class));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userDTO(userServiceImpl.getUserByUsername(user.getUsername()))
                .build();
    }

    @Override
    public AuthenticationResponse login(final LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword()));
        var user = modelMapper.map(userServiceImpl.getUserByUsername(loginDTO.getUsername()), User.class);
        var jwtToken = jwtServiceImpl.generateToken(modelMapper.map(user, User.class));
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userDTO(userServiceImpl.getUserByUsername(loginDTO.getUsername()))
                .build();
    }
}
