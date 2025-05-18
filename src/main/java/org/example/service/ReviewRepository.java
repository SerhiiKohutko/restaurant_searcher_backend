package org.example.service;

import org.example.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByPlaceIdAAndOrderByDateCreatedDesc(Long placeId);
}
