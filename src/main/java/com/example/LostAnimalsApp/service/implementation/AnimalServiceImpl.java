package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Animal;
import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.model.User;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.repository.ImageRepository;
import com.example.LostAnimalsApp.repository.UserRepository;
import com.example.LostAnimalsApp.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public AnimalDTO createAnimal(AnimalDTO animalDTO) {
        if (checkFields(animalDTO)) {
            var animal = Animal.builder()
                    .name(animalDTO.getName())
                    .breed(animalDTO.getBreed())
                    .color(animalDTO.getColor())
                    .species(animalDTO.getSpecies())
                    .isFound(animalDTO.getIsFound())
                    .image(imageRepository.findImageByFileName(animalDTO.getImage().getFileName()).orElse(null))
                    .user(userRepository.findByUsername(animalDTO.getUser().getUsername()).orElse(null))
                    .age(animalDTO.getAge())
                    .build();
            animalRepository.save(animal);
            return modelMapper.map(animal, AnimalDTO.class);
        } else throw new ResourceNotFoundException("The animal cannot be created!");
    }

    @Override
    public AnimalDTO updateAnimal(AnimalDTO animalDTO) {
        Animal animalToUpdate = animalRepository.findAnimalByImageFileName(animalDTO.getImage().getFileName()).orElse(null);
        if (animalToUpdate != null && checkFields(animalDTO)) {
            animalToUpdate = Animal.builder()
                    .name(animalDTO.getName())
                    .breed(animalDTO.getBreed())
                    .color(animalDTO.getColor())
                    .species(animalDTO.getSpecies())
                    .isFound(animalDTO.getIsFound())
                    .image(imageRepository.findImageByFileName(animalDTO.getImage().getFileName()).orElse(null))
                    .user(userRepository.findByUsername(animalDTO.getUser().getUsername()).orElse(null))
                    .age(animalDTO.getAge())
                    .build();
            animalRepository.save(animalToUpdate);
            return modelMapper.map(animalToUpdate, AnimalDTO.class);
        } else throw new ResourceNotFoundException("The animal cannot be updated!");
    }

    @Override
    public AnimalDTO deleteAnimal(Long animalId) {
        return null; // TODO : rethink the delete param; should I send the id?? <-> animalDTO has id
    }

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository
                .findAll()
                .stream()
                .map(animal -> modelMapper.map(animal, AnimalDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AnimalDTO getAnimalById(Long animalId) {
        return null;
    }

    private boolean checkFields(final AnimalDTO animalDTO) {
        if (animalDTO.getName() == null || animalDTO.getName().isBlank()) {
            return false;
        }
        if (animalDTO.getBreed() == null || animalDTO.getBreed().isBlank()) {
            return false;
        }
        if (animalDTO.getColor() == null || animalDTO.getColor().isBlank()) {
            return false;
        }
        if (animalDTO.getSpecies() == null || animalDTO.getSpecies().isBlank()) {
            return false;
        }
        if (animalDTO.getIsFound() == null) {
            return false;
        }
        if (animalDTO.getImage() == null || imageRepository.findImageByFileName(animalDTO.getImage().getFileName()).isEmpty()) {
            return false;
        }
        if (animalDTO.getUser() == null || userRepository.findByUsername(animalDTO.getUser().getUsername()).isEmpty()) {
            return false;
        }

        return true;
    }
}
