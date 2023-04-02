package com.example.LostAnimalsApp.dto;

import com.example.LostAnimalsApp.enums.Role;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmedPassword;
    private Role role;
}
