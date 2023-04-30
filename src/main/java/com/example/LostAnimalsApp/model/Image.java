package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Column(nullable = false, unique = true)
    private String fileName;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    @Column
    private String description;

    @Transient
    private MultipartFile file;

}
