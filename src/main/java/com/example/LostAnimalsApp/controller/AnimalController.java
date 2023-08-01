package com.example.LostAnimalsApp.controller;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.dto.AnimalInfoDTO;
import com.example.LostAnimalsApp.service.AnimalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/animal")
@CrossOrigin("*")
public class AnimalController {

	@Autowired
	private AnimalService animalService;

	@GetMapping("/{isFound}")
	public ResponseEntity<List<AnimalInfoDTO>> getAnimalsImages(@PathVariable("isFound") final String isFound) {
		boolean animalIsFound = "true".equals(isFound);
		try {
			return ResponseEntity.ok(animalService.getLostOrFoundAnimalsImages(animalIsFound));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/similar/{filename}")
	public ResponseEntity<List<AnimalInfoDTO>> getSimilarImages(@PathVariable("filename") final String filename) {
		try {
			return ResponseEntity.ok(animalService.getSimilarAnimalsImages(filename));
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@GetMapping("/species/{filename}")
	public ResponseEntity<String> getAnimalSpecies(@PathVariable("filename") final String filename) {
		return ResponseEntity.ok(animalService.getAnimalSpecies(filename));
	}

	@GetMapping
	public ResponseEntity<List<AnimalInfoDTO>> getAllAnimalsImages() {
		try {
			return ResponseEntity.ok(animalService.getAllAnimalsImages());
		} catch (IOException | InterruptedException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/pets/{username}")
	public ResponseEntity<List<AnimalInfoDTO>> getUsersAnimals(@PathVariable("username") final String username) {
		final List<AnimalInfoDTO> images = animalService.getImagesByUser(username);
		return ResponseEntity.ok(images);
	}

	@PostMapping("/{isCorrectSpecies}")
	public ResponseEntity<String> createAnimal(@RequestBody final AnimalDTO animalDTO,
			@PathVariable("isCorrectSpecies") final String isCorrectSpecies) {
		try {
			animalService.createAnimal(animalDTO, isCorrectSpecies);
			return ResponseEntity.ok("Animal data added successfully!");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Animal cannot be created!");
		}
	}

	@DeleteMapping("/{filename}")
	public ResponseEntity<String> deleteAnimal(@PathVariable("filename") final String filename) {
		try {
			return ResponseEntity.ok(animalService.deleteAnimal(filename));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Animal cannot be deleted!");
		}
	}

}
