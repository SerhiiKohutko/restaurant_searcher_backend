package org.example.controllers;

import org.example.dto.PlaceDto;
import org.example.service.PlacesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/places")
public class PlacesController {

    @Autowired
    private PlacesService placesService;

    @GetMapping("/{placeId}")
    public ResponseEntity<PlaceDto> getPlaceDetails(@PathVariable String placeId){
        PlaceDto place = placesService.getPlaceDetailsById(placeId);
        return ResponseEntity.ok(place);
    }
}
