package org.example.service;

import org.example.entity.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public List<Review> getReviewsByPlaceId(String placeId) {

        List<Review> reviews =
                reviewRepository.findAllByPlaceIdAAndOrderByDateCreatedDesc(placeId);

        return List.of();
    }
}
