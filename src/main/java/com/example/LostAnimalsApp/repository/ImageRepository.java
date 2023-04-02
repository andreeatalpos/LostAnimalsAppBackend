package com.example.LostAnimalsApp.repository;

import com.example.LostAnimalsApp.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findImageByFileName(final String fileName);
}
