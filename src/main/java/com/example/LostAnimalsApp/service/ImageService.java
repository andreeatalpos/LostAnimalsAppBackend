package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.ImageDTO;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    ImageDTO createImage(final ImageDTO imageDTO) throws IOException;
    ImageDTO updateImage(final ImageDTO imageDTO);
    List<ImageDTO> getAllImages();
    List<ImageDTO> getAllLostAnimalsImages();
    List<ImageDTO> getAllFoundAnimalsImages();
    ImageDTO getImageById(final Long imageId);
    ImageDTO deleteImage(final Long imageId);
}
