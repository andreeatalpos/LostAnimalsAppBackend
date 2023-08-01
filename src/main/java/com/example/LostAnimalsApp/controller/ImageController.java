package com.example.LostAnimalsApp.controller;

import com.example.LostAnimalsApp.dto.ImageUploadDTO;
import com.example.LostAnimalsApp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/image")
@CrossOrigin("*")
public class ImageController {

	@Autowired
	private ImageService imageService;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadImage(@ModelAttribute ImageUploadDTO imageUploadDTO) {
		try {
			return ResponseEntity.ok(imageService.createImage(imageUploadDTO));
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
		}

	}

	@GetMapping
	public ResponseEntity<List<ImageUploadDTO>> getAllImages() {
		return ResponseEntity.ok(imageService.getAllImages());
	}
	@DeleteMapping
	public ResponseEntity<String> deleteAllImages() {
		imageService.deleteAllImages();
		return ResponseEntity.ok("Images deleted successfully");
	}

	@DeleteMapping("/{filename}")
	public ResponseEntity<String> deleteImageByFilename(@PathVariable("filename") final String filename) {
		try {
			return ResponseEntity.ok(imageService.deleteImage(filename));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image");
		}
	}
}
