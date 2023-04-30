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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    @Override
    public ImageDTO createImage(ImageDTO imageDTO) throws IOException {
        System.out.println(imageDTO.getDescription());
        if (checkFields(imageDTO)) {
            var imageToCreate = Image.builder()
                    .uploadedAt(LocalDateTime.now())
                    .description(imageDTO.getDescription())
                    .build();
            // define the folder path where the image will be saved
            String folderPath = "src/main/resources/images";
            String randomString = UUID.randomUUID().toString().substring(0, 8);
            String fileName = "animal_" + randomString + ".jpeg";
            imageToCreate.setFileName(fileName);
            // create a File object with the folder path and file name
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File imageFile = new File(folder, fileName);
            // write the file content to the File object
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(imageDTO.getFile().getBytes());
            outputStream.flush();
            outputStream.close();
            imageRepository.save(imageToCreate);
            return modelMapper.map(imageToCreate, ImageDTO.class);
        } else throw new ResourceNotFoundException("The image couldn't be created!"); // todo: create more exceptions
    }

    @Override
    public ImageDTO updateImage(ImageDTO imageDTO) {
        Image imageToUpdate = imageRepository.findById(imageDTO.getImageId()).orElse(null);
        if(imageToUpdate != null && checkFields(imageDTO)) {
            imageToUpdate = Image.builder()
                    .uploadedAt(LocalDateTime.now())
                    .description(imageDTO.getDescription())
                    .file(imageToUpdate.getFile())
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
                .map(image -> modelMapper.map(image, ImageDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> getAllFoundAnimalsImages() {
        return imageRepository
                .findAll()
                .stream()
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
        if (imageDTO.getFile() == null) {
            return false;
        }
        return true;
    }
}
