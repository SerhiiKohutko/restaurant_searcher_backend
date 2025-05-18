package org.example.service;

import org.example.dto.PlaceDto;
import org.example.dto.PlaceMarker;
import org.example.entity.Place;

import java.util.List;
import java.util.Optional;

public interface PlacesService {

    PlaceDto getPlaceDetailsById(String placeId);

    List<PlaceMarker> getAllPlaces();

    void isPlaceIdValid(String placeId);

    Optional<Place> getPlaceById(String placeId);
}
