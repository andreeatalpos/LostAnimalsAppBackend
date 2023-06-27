package com.example.LostAnimalsApp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalInfoDTO {
	private String animalInfo;
	private String species;
	private String breed;
	private String fullName;
	private Long phoneNumber;
	private String email;
	private Boolean isFound;
	private String filename;
	private byte[] file;
}
