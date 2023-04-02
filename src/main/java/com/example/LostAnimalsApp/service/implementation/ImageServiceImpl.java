package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.ImageDTO;
import com.example.LostAnimalsApp.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Override
    public ImageDTO createImage(ImageDTO imageDTO) {
        return null;
    }

    @Override
    public ImageDTO updateImage(ImageDTO imageDTO) {
        return null;
    }

    @Override
    public List<ImageDTO> getAllImages() {
        return null;
    }

    @Override
    public List<ImageDTO> getAllLostAnimalsImages() {
        return null;
    }

    @Override
    public List<ImageDTO> getAllFoundAnimalsImages() {
        return null;
    }

    @Override
    public ImageDTO getImageById(Long imageId) {
        return null;
    }

    @Override
    public ImageDTO deleteImage(Long imageId) {
        return null;
    }
}
