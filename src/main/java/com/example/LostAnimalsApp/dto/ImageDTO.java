package com.example.LostAnimalsApp.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageDTO {
    private Long imageId;
    private byte[] imageData;
    private String fileName;
    private LocalDateTime uploadedAt;
    private String description;
    private AnimalDTO animal;
}
