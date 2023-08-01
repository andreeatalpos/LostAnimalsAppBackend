package com.example.LostAnimalsApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long userId;
    private String fullName;
    private String email;
    private String username;
    private Long phoneNumber;
}