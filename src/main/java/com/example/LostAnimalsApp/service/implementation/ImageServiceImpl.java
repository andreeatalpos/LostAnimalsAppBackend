package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.dto.ImageDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Animal;
import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.repository.ImageRepository;
import com.example.LostAnimalsApp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    @Override
    public ImageDTO createImage(ImageDTO imageDTO) {
        if (checkFields(imageDTO)) {
            var imageToCreate = Image.builder()
                    .imageData(imageDTO.getImageData())
                    .fileName(imageDTO.getFileName())
                    .uploadedAt(imageDTO.getUploadedAt())
                    .description(imageDTO.getDescription())
                    .animal(modelMapper.map(imageDTO.getAnimal(), Animal.class))
                    .build();
            imageRepository.save(imageToCreate);
            return modelMapper.map(imageToCreate, ImageDTO.class);
        } else throw new ResourceNotFoundException("The image couldn't be created!"); // todo: create more exceptions
    }

    @Override
    public ImageDTO updateImage(ImageDTO imageDTO) {
        Image imageToUpdate = imageRepository.findById(imageDTO.getImageId()).orElse(null);
        if(imageToUpdate != null && checkFields(imageDTO)) {
            imageToUpdate = Image.builder()
                    .imageData(imageDTO.getImageData())
                    .fileName(imageDTO.getFileName())
                    .uploadedAt(imageDTO.getUploadedAt())
                    .description(imageDTO.getDescription())
                    .animal(modelMapper.map(imageDTO.getAnimal(), Animal.class))
                    .build();
            imageRepository.save(imageToUpdate);
            return modelMapper.map(imageToUpdate, ImageDTO.class);
        } else throw new ResourceNotFoundException("The image couldn't be created!");
    }

    @Override
    public List<ImageDTO> getAllImages() {
        return imageRepository
                .findAll()
                .stream()
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> getAllLostAnimalsImages() {
        return imageRepository
                .findAll()
                .stream()
                .filter(image -> image.getAnimal().getIsFound().equals(false))
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> getAllFoundAnimalsImages() {
        return imageRepository
                .findAll()
                .stream()
                .filter(image -> image.getAnimal().getIsFound().equals(true))
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageDTO getImageById(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            return modelMapper.map(image, ImageDTO.class);
        } else throw new ResourceNotFoundException("The image with this ID doesn't exists!");
    }

    @Override
    public ImageDTO deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            imageRepository.delete(image);
            return modelMapper.map(image, ImageDTO.class);
        } else throw new ResourceNotFoundException("The image with this ID doesn't exists!");
    }

    //todo think about when we create the animal, should it exists when I add the image? yes for now
    private boolean checkFields(final ImageDTO imageDTO) {
        if (imageDTO.getImageData() == null) {
            return false;
        }
        if (imageDTO.getFileName() == null || imageDTO.getFileName().isBlank()) {
            return false;
        }
        if (imageDTO.getAnimal() == null || animalRepository.findById(imageDTO.getAnimal().getAnimalId()).isEmpty()) {
            return false;
        }
        if (imageDTO.getUploadedAt() == null) {
            return false;
        }
        return true;
    }
}
