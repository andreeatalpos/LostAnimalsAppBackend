package com.example.LostAnimalsApp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReportDTO {
    private String title;
    private String description;
    private UserDTO user;
    private AnimalDTO animal;
}
