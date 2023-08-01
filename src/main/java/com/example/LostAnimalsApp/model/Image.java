package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

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

    @Column
    private String description;

}
