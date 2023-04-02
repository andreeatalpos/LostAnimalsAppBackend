package com.example.LostAnimalsApp.dto;

import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.model.User;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalDTO {
    private String name;
    private String species;
    private String breed;
    private String color;
    private Integer age;
    private Boolean isFound;
    private UserDTO user;
    private ImageDTO image;
}
