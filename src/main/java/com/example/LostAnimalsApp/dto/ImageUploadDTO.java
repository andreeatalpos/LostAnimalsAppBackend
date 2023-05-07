package com.example.LostAnimalsApp.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageUploadDTO {
    private Long imageId;
    private String description;
    private MultipartFile file;
}
