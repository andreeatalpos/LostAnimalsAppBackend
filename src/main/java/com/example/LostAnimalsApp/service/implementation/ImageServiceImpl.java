package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.ImageUploadDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.repository.AnimalRepository;
import com.example.LostAnimalsApp.repository.ImageRepository;
import com.example.LostAnimalsApp.service.ImageService;
import com.example.LostAnimalsApp.util.ImageResizer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final AnimalRepository animalRepository;
    private final static String IMAGES_FOLDER_PATH = "src/main/resources/images";
    private final ModelMapper modelMapper;
    private final ImageResizer imageResizer;
    @Override
    public String createImage(ImageUploadDTO imageUploadDTO) throws IOException {
        System.out.println(imageUploadDTO.getDescription());
        if (checkFields(imageUploadDTO)) {
            var imageToCreate = Image.builder()
                    .uploadedAt(LocalDateTime.now())
                    .description(imageUploadDTO.getDescription())
                    .build();
            // Define the folder path where the image will be saved
            String randomString = UUID.randomUUID().toString().substring(0, 8);
            String fileName = "animal_" + randomString + ".jpeg";
            imageToCreate.setFileName(fileName);

            // Create a File object with the folder path and file name
            File folder = new File(IMAGES_FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            File imageFile = new File(folder, fileName);

            // Resize the image
            byte[] resizedImageData = imageResizer.resizeImage(imageUploadDTO.getFile().getBytes(), 224, 224);

            // Write the resized image data to the File object
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            outputStream.write(resizedImageData);
            outputStream.flush();
            outputStream.close();

            imageRepository.save(imageToCreate);
            return fileName;
        } else {
            throw new ResourceNotFoundException("The image couldn't be created!"); // todo: create more exceptions
        }
    }

    @Override
    public ImageUploadDTO updateImage(ImageUploadDTO imageUploadDTO) {
        Image imageToUpdate = imageRepository.findById(imageUploadDTO.getImageId()).orElse(null);
        if(imageToUpdate != null && checkFields(imageUploadDTO)) {
            imageToUpdate = Image.builder()
                    .uploadedAt(LocalDateTime.now())
                    .description(imageUploadDTO.getDescription())
                    .file(imageToUpdate.getFile())
                    .build();
            imageRepository.save(imageToUpdate);
            return modelMapper.map(imageToUpdate, ImageUploadDTO.class);
        } else throw new ResourceNotFoundException("The image couldn't be created!");
    }

    @Override
    public List<ImageUploadDTO> getAllImages() {
        return imageRepository
                .findAll()
                .stream()
                .map(image -> modelMapper.map(image, ImageUploadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ImageUploadDTO getImageById(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            return modelMapper.map(image, ImageUploadDTO.class);
        } else throw new ResourceNotFoundException("The image with this ID doesn't exists!");
    }

    @Override
    public ImageUploadDTO deleteImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElse(null);
        if (image != null) {
            imageRepository.delete(image);
            return modelMapper.map(image, ImageUploadDTO.class);
        } else throw new ResourceNotFoundException("The image with this ID doesn't exists!");
    }

    @Override
    public void deleteAllImages() {
        File folder = new File(IMAGES_FOLDER_PATH);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        file.delete();
                    }
                }
            }
        }
        List<Image> images = imageRepository.findAll();
        imageRepository.deleteAll(images);
    }

    //todo think about when we create the animal, should it exists when I add the image? yes for now
    private boolean checkFields(final ImageUploadDTO imageUploadDTO) {
        if (imageUploadDTO.getFile() == null) {
            return false;
        }
        return true;
    }
}
