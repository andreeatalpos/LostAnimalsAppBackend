package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.AnimalDTO;
import com.example.LostAnimalsApp.dto.AnimalInfoDTO;
import com.example.LostAnimalsApp.dto.ImageDTO;
import com.example.LostAnimalsApp.dto.ImageUploadDTO;

import java.io.IOException;
import java.util.List;

public interface AnimalService {
    AnimalDTO createAnimal(final AnimalDTO animalDTO, final String isCorrectSpecies) throws IOException, InterruptedException;
    String getAnimalSpecies(final String filename);
    List<AnimalInfoDTO> getLostOrFoundAnimalsImages(boolean isFound) throws IOException, InterruptedException;

    List<AnimalInfoDTO> getSimilarAnimalsImages(final String filename) throws IOException, InterruptedException;
    List<AnimalInfoDTO> getImagesByUser(final String username);

    List<AnimalInfoDTO> getAllAnimalsImages() throws IOException, InterruptedException;
    String deleteAnimal(final String filename);
}
