package com.example.LostAnimalsApp.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="report")
public class Report {

    @Id
    @GeneratedValue
    @Column(name="report_id")
    private Long reportId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToOne
    @JoinColumn(name="animal_id")
    private Animal animal;

}
