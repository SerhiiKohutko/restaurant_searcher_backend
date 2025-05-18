package org.example.service;

import org.example.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, String> {

    Optional<Place> getPlaceByPlaceId(String id);
}
