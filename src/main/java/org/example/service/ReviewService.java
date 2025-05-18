package org.example.service;

import org.example.entity.Review;
import org.example.request.ReviewCreationRequest;

import java.util.List;

public interface ReviewService {
    List<Review> getReviewsByPlaceId(String placeId);
    Review createReview(ReviewCreationRequest request);
}
