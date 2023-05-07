package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.ImageUploadDTO;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    String createImage(final ImageUploadDTO imageUploadDTO) throws IOException;
    ImageUploadDTO updateImage(final ImageUploadDTO imageUploadDTO);
    List<ImageUploadDTO> getAllImages();
    ImageUploadDTO getImageById(final Long imageId);
    ImageUploadDTO deleteImage(final Long imageId);
    void deleteAllImages();
}
