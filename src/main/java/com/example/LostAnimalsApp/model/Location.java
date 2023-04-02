package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="location")
public class Location {

    @Id
    @GeneratedValue
    @Column(name="location_id")
    private Long locationId;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private Integer streetNumber;

}
