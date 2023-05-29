package com.example.LostAnimalsApp.controller;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.dto.AnimalInfoDTO;
import com.example.LostAnimalsApp.dto.ImageDTO;
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
        }
    }

    @GetMapping
    public ResponseEntity<List<AnimalInfoDTO>> getAllAnimalsImages() {
        try {
            return ResponseEntity.ok(animalService.getAllAnimalsImages());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/pets/{username}")
    public ResponseEntity<List<ImageDTO>> getUsersAnimals(@PathVariable("username") final String username) {
        List<ImageDTO> images = animalService.getImagesByUser(username);
        return ResponseEntity.ok(images);
    }

    @PostMapping
    public ResponseEntity<String> createAnimal(@RequestBody final AnimalDTO animalDTO) {
        try
        {
            animalService.createAnimal(animalDTO);
            return ResponseEntity.ok("Animal data added successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Animal cannot be created!");
        }
    }

}
