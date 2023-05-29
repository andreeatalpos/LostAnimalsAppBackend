package com.example.LostAnimalsApp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AnimalInfoDTO {
	private String animalInfo;
	private String fullName;
	private Long phoneNumber;
	private String email;
	private byte[] file;
}
