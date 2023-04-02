package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.LoginDTO;
import com.example.LostAnimalsApp.util.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(final AuthDTO user);
    AuthenticationResponse login(final LoginDTO loginDTO);
}
