package com.example.LostAnimalsApp.repository;

import com.example.LostAnimalsApp.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {
    Optional<Animal> findAnimalByImageFileName(final String imageFileName);
    List<Animal> findAllByIsFound(boolean isFound);

}
