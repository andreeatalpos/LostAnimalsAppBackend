package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.AnimalDTO;

import java.util.List;

public interface AnimalService {
    AnimalDTO createAnimal(final AnimalDTO animalDTO);
    AnimalDTO updateAnimal(final AnimalDTO animalDTO);
    AnimalDTO deleteAnimal(final Long animalId);
    List<AnimalDTO> getAllAnimals();
    AnimalDTO getAnimalById(final Long animalId);
}
