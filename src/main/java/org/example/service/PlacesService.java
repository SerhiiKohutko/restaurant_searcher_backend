package org.example.service;

import org.example.dto.PlaceDto;
import org.example.dto.PlaceMarker;

import java.util.List;

public interface PlacesService {

    PlaceDto getPlaceDetailsById(String placeId);

    List<PlaceMarker> getAllPlaces();

    void isPlaceIdValid(String placeId);
}
