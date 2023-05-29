package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.dto.AnimalInfoDTO;
import com.example.LostAnimalsApp.dto.ImageDTO;
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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    private final static String IMAGES_FOLDER_PATH = "src/main/resources/images/";


    @Override
    public AnimalDTO createAnimal(final AnimalDTO animalDTO) {
        if (checkFields(animalDTO)) {
            var animal = Animal.builder()
                    .name(animalDTO.getAnimalName())
                    .animalInfo(animalDTO.getAnimalInfo())
                    .breed(animalDTO.getBreed())
                    .color(animalDTO.getColor())
                    .species(animalDTO.getSpecies())
                    .isFound(animalDTO.getIsFound())
                    .image(imageRepository.findImageByFileName(animalDTO.getFileName()).orElse(null))
                    .user(userRepository.findByUsername(animalDTO.getUsername()).orElse(null))
                    .age(animalDTO.getAge())
                    .build();
            animalRepository.save(animal);
            return modelMapper.map(animal, AnimalDTO.class);
        } else throw new ResourceNotFoundException("The animal cannot be created!");
    }

    @Override
    public AnimalDTO updateAnimal(final AnimalDTO animalDTO) {
        Animal animalToUpdate = animalRepository.findAnimalByImageFileName(animalDTO.getFileName()).orElse(null);
        if (animalToUpdate != null && checkFields(animalDTO)) {
            animalToUpdate = Animal.builder()
                    .name(animalDTO.getAnimalName())
                    .animalInfo(animalToUpdate.getAnimalInfo())
                    .breed(animalDTO.getBreed())
                    .color(animalDTO.getColor())
                    .species(animalDTO.getSpecies())
                    .isFound(animalDTO.getIsFound())
                    .image(imageRepository.findImageByFileName(animalDTO.getFileName()).orElse(null))
                    .user(userRepository.findByUsername(animalDTO.getUsername()).orElse(null))
                    .age(animalDTO.getAge())
                    .build();
            animalRepository.save(animalToUpdate);
            return modelMapper.map(animalToUpdate, AnimalDTO.class);
        } else throw new ResourceNotFoundException("The animal cannot be updated!");
    }

    @Override
    public AnimalDTO deleteAnimal(final Long animalId) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResourceNotFoundException("The animal with the provided ID doesn't exists!");
        }
        animalRepository.delete(animal);
        return modelMapper.map(animal, AnimalDTO.class); // TODO : rethink the delete param; should I send the id?? <-> animalDTO has id
    }

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository
                .findAll()
                .stream()
                .map(animal -> modelMapper.map(animal, AnimalDTO.class))
                .collect(Collectors.toList());
    }

    public List<AnimalInfoDTO> getAllAnimalsImages() throws IOException {
        List<AnimalInfoDTO> imageDTOList = new ArrayList<>();
        final List<Animal> foundAnimals = animalRepository.findAll();
        return getImageDTOS(imageDTOList, foundAnimals);
    }

    @Override
    public AnimalDTO getAnimalById(final Long animalId) {
        Animal animal = animalRepository.findById(animalId).orElse(null);
        if (animal == null) {
            throw new ResourceNotFoundException("The animal with the provided ID doesn't exists!");
        }
        return modelMapper.map(animal, AnimalDTO.class);
    }

    @Override
    public List<AnimalInfoDTO> getLostOrFoundAnimalsImages(final boolean isFound) throws IOException {
        List<AnimalInfoDTO> imageDTOList = new ArrayList<>();
        final List<Animal> foundAnimals = animalRepository.findAllByIsFound(isFound);
        return getImageDTOS(imageDTOList, foundAnimals);
    }

    private List<AnimalInfoDTO> getImageDTOS(final List<AnimalInfoDTO> imageDTOList, final List<Animal> foundAnimals)
            throws IOException {
        for (Animal animal : foundAnimals) {
            User user = animal.getUser();
            Image image = animal.getImage();
            if (image != null) {
                AnimalInfoDTO animalInfo = AnimalInfoDTO.builder()
                        .animalInfo(animal.getAnimalInfo())
                        .email(user.getEmail())
                        .fullName(user.getFullName())
                        .phoneNumber(user.getPhoneNumber())
                        .build();
                byte[] imageData = Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH + image.getFileName()));

                animalInfo.setFile(imageData);
                imageDTOList.add(animalInfo);
            }
        }
        return imageDTOList;
    }

    @Override
    public List<ImageDTO> getImagesByUser(final String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            final List<Animal> allAnimals = animalRepository.findAll();
            return allAnimals.stream()
                    .filter(animal -> user.get().equals(animal.getUser()))
                    .map(Animal::getImage)
                    .map(image -> {
                        try {
                            return buildImageDTO(image);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }

    private ImageDTO buildImageDTO(final Image image) throws IOException {
        if (image != null) {
            ImageDTO imageDTO = ImageDTO.builder()
                    .imageId(image.getImageId())
                    .description(image.getDescription())
                    .build();
            byte[] imageData = Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH + image.getFileName()));
            imageDTO.setFile(imageData);
            return imageDTO;
        }
        return null;
    }

    private boolean checkFields(final AnimalDTO animalDTO) {
        if (animalDTO.getAnimalInfo() == null || animalDTO.getAnimalInfo().isBlank()) {
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
//        if (animalDTO.getFileName() == null || imageRepository.findImageByFileName(animalDTO.getFileName()).isEmpty()) {
//            return false;
//        }
        if (animalDTO.getUsername() == null || userRepository.findByUsername(animalDTO.getUsername()).isEmpty()) {
            return false;
        }

        return true;
    }
}
