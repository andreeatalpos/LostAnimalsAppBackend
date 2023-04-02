package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.AuthDTO;
import com.example.LostAnimalsApp.dto.UserDTO;

public interface UserService {
    UserDTO createUser(final AuthDTO authUser);
    UserDTO getUserByUsername(final String username);
    UserDTO updateUser(final AuthDTO authUser);
}
