package org.example.service;

import org.example.dto.PlaceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlacesServiceImpl implements PlacesService{

    @Autowired
    private GoogleMapService googleMapService;

    @Override
    public PlaceDto getPlaceDetailsById(String placeId) {
        return googleMapService.getPlaceDetailsFromGoogleMaps(placeId);
    }
}
