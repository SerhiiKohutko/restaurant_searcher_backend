package org.example.service;

import org.example.dto.PlaceDto;

public interface PlacesService {

    PlaceDto getPlaceDetailsById(String placeId);
}
