package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.ImageUploadDTO;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    String createImage(final ImageUploadDTO imageUploadDTO) throws IOException;
    List<ImageUploadDTO> getAllImages();
    void deleteAllImages();

    String deleteImage(final String fileName);
}
