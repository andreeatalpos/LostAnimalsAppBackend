package com.example.LostAnimalsApp.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageDTO {
	private Long imageId;
	private String description;
	private byte[] file;
}
