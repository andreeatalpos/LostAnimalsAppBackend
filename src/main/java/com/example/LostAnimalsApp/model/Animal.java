package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String name; // poate avea zgarda cu nume, sau cel care l-a pierdut sa zica ca raspunde la numele....

    @Column
    private String animalInfo;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private String breed;

    @Column
    private Integer age; // varsta exacta de catre proprietar, sau aprox de la cel care il gaseste

    @Column(nullable = false)
    private Boolean isFound; // true if animal is found by someone, false if it's lost

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name="image_id")
    private Image image;

}
