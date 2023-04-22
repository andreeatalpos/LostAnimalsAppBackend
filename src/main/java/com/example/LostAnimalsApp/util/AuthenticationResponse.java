package com.example.LostAnimalsApp.util;

import com.example.LostAnimalsApp.dto.UserDTO;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    private String token;
    private UserDTO userDTO;
}
