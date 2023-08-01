package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.ImageUploadDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Image;
import com.example.LostAnimalsApp.repository.ImageRepository;
import com.example.LostAnimalsApp.service.ImageService;
import com.example.LostAnimalsApp.util.ImageResizer;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final static String IMAGES_FOLDER_PATH = "src/main/resources/images";
    private final ModelMapper modelMapper;
    private final ImageResizer imageResizer;
    @Override
    public String createImage(final ImageUploadDTO imageUploadDTO) throws IOException {
        if (checkFields(imageUploadDTO)) {
            var imageToCreate = Image.builder()
                    .description(imageUploadDTO.getDescription())
                    .build();
            final String randomString = UUID.randomUUID().toString().substring(0, 8);
            final String fileName = "animal_" + randomString + ".jpeg";
            imageToCreate.setFileName(fileName);

            final File folder = new File(IMAGES_FOLDER_PATH);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            final File imageFile = new File(folder, fileName);

            final byte[] resizedImageData = imageResizer.resizeImage(imageUploadDTO.getFile().getBytes(), 224, 224);

            final FileOutputStream outputStream = new FileOutputStream(imageFile);
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
    public List<ImageUploadDTO> getAllImages() {
        return imageRepository
                .findAll()
                .stream()
                .map(image -> modelMapper.map(image, ImageUploadDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public String deleteImage(final String fileName) {
        final Image image = imageRepository.findImageByFileName(fileName).orElse(null);
        if (image != null) {
            imageRepository.delete(image);
            return fileName;
        } else throw new ResourceNotFoundException("The image with this ID doesn't exists!");
    }

    @Override
    public void deleteAllImages() {
        final File folder = new File(IMAGES_FOLDER_PATH);
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
        final List<Image> images = imageRepository.findAll();
        imageRepository.deleteAll(images);
    }

    private boolean checkFields(final ImageUploadDTO imageUploadDTO) {
        if (imageUploadDTO.getFile() == null) {
            return false;
        }
        return true;
    }
}
