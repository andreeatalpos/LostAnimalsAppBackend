package com.example.LostAnimalsApp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalDTO {
    private Long animalId;
    private String animalName;
    private String animalInfo;
    private String species;
    private String breed;
    private String color;
    private Integer age;
    private Boolean isFound;
    private String username;
    private String fileName;
}
