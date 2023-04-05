package com.example.LostAnimalsApp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LocationDTO {
    private Long locationId;
    private String country;
    private String city;
    private String street;
    private Integer streetNumber;
}
