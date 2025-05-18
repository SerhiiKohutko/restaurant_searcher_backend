package org.example.service;

import org.example.dto.PlaceDto;
import org.example.dto.PlaceMarker;
import org.example.entity.Place;
import org.example.exceptions.InvalidPlaceIdException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlacesServiceImpl implements PlacesService{

    @Autowired
    private GoogleMapService googleMapService;
    @Autowired
    private PlaceRepository placeRepository;

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

    @Override
    public Place createPlace(Place place) {
        return placeRepository.save(place);
    }

    @Override
    public Optional<Place> getPlaceById(String placeId) {
        return Optional.empty();
    }
}
