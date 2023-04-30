package com.example.LostAnimalsApp.controller;

import com.example.LostAnimalsApp.dto.ImageDTO;
import com.example.LostAnimalsApp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@ModelAttribute ImageDTO imageDTO) {
		try {
			imageService.createImage(imageDTO);
			return ResponseEntity.ok("Image uploaded successfully");
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
		}

	}
}
