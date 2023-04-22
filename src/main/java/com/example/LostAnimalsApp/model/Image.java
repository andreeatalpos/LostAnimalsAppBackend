package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="image")
public class Image {

    @Id
    @GeneratedValue
    @Column(name="image_id")
    private Long imageId;

    @Lob
    @Column(nullable = false)
    private Byte[] imageData;

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column
    private String description;

    @OneToOne
    @JoinColumn(name = "animal_id")
    private Animal animal;

}
