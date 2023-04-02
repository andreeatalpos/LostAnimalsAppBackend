package com.example.LostAnimalsApp.service;

import com.example.LostAnimalsApp.dto.LocationDTO;

public interface LocationService {
    LocationDTO createLocation(final LocationDTO locationDTO);
    LocationDTO updateLocation(final LocationDTO locationDTO);
    LocationDTO getLocationById(final Long locationId);
    LocationDTO deleteLocation(final Long locationId);
}
