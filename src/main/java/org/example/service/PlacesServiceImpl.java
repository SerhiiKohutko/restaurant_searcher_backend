package org.example.service;

import org.example.dto.PlaceDto;
import org.example.dto.PlaceMarker;
import org.example.exceptions.InvalidPlaceIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlacesServiceImpl implements PlacesService{

    @Autowired
    private GoogleMapService googleMapService;

    @Override
    public PlaceDto getPlaceDetailsById(String placeId) {
        return googleMapService.getPlaceDetailsFromGoogleMaps(placeId);
    }

    @Override
    public List<PlaceMarker> getAllPlaces() {
        return googleMapService.getAllPlaces();
    }

    @Override
    public void isPlaceIdValid(String placeId){
        PlaceDto placeDto = googleMapService.getPlaceDetailsFromGoogleMaps(placeId);
        if (placeDto.getAddress() == null || placeDto.getName() == null){
            throw new InvalidPlaceIdException(placeId);
        }
    }
}
