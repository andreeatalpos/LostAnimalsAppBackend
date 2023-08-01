package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="animal")
public class Animal {

    @Id
    @GeneratedValue
    @Column(name="animal_id")
    private Long animalId;

    @Column
    private String name;

    @Column
    private String animalInfo;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String breed;

    @Column
    private Integer age;

    @Column(nullable = false)
    private Boolean isFound;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="image_id")
    private Image image;

}
