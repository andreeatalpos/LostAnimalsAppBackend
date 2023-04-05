package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.LocationDTO;
import com.example.LostAnimalsApp.exception.ResourceNotFoundException;
import com.example.LostAnimalsApp.model.Location;
import com.example.LostAnimalsApp.repository.LocationRepository;
import com.example.LostAnimalsApp.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;
    private final ModelMapper modelMapper;
    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        if (checkFields(locationDTO)) {
            var location = Location.builder()
                    .country(locationDTO.getCountry())
                    .city(locationDTO.getCity())
                    .street(locationDTO.getStreet())
                    .streetNumber(locationDTO.getStreetNumber())
                    .build();
            locationRepository.save(location);
            return modelMapper.map(location, LocationDTO.class);
        } else throw new ResourceNotFoundException("The location cannot be created!");
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO) {
        Location locationToUpdate = locationRepository.findById(locationDTO.getLocationId()).orElse(null);
        if (locationToUpdate != null && checkFields(locationDTO)) {
            locationToUpdate = Location.builder()
                    .country(locationDTO.getCountry())
                    .city(locationDTO.getCity())
                    .street(locationDTO.getStreet())
                    .streetNumber(locationDTO.getStreetNumber())
                    .build();
            locationRepository.save(locationToUpdate);
            return modelMapper.map(locationToUpdate, LocationDTO.class);
        } else throw new ResourceNotFoundException("The location cannot be updated!");
    }

    @Override
    public LocationDTO getLocationById(Long locationId) {
        Location location = locationRepository.findById(locationId).orElse(null);
        if (location != null) {
            return modelMapper.map(location, LocationDTO.class);
        } else throw new ResourceNotFoundException("The location with the given ID doesn't exists!");
    }

    @Override
    public LocationDTO deleteLocation(Long locationId) {
        Location location = locationRepository.findById(locationId).orElse(null);
        if (location != null) {
            locationRepository.delete(location);
            return modelMapper.map(location, LocationDTO.class);
        } else throw new ResourceNotFoundException("The location with the given ID doesn't exists!");
    }

    private boolean checkFields(final LocationDTO locationDTO) {
        if (locationDTO.getCountry() == null || locationDTO.getCountry().isBlank()) {
            return false;
        }
        if (locationDTO.getCity() == null || locationDTO.getCity().isBlank()) {
            return false;
        }
        if (locationDTO.getStreet() == null || locationDTO.getStreet().isBlank()) {
            return false;
        }
        if (locationDTO.getStreetNumber() == null) {
            return false;
        }
        return true;
    }
}
