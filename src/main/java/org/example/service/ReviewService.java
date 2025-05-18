package org.example.service;

import org.example.entity.Review;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByPlaceId(String placeId);
}
