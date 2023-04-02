package com.example.LostAnimalsApp.service.implementation;

import com.example.LostAnimalsApp.dto.LocationDTO;
import com.example.LostAnimalsApp.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    @Override
    public LocationDTO createLocation(LocationDTO locationDTO) {
        return null;
    }

    @Override
    public LocationDTO updateLocation(LocationDTO locationDTO) {
        return null;
    }

    @Override
    public LocationDTO getLocationById(Long locationId) {
        return null;
    }

    @Override
    public LocationDTO deleteLocation(Long locationId) {
        return null;
    }
}
