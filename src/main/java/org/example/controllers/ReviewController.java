package org.example.controllers;

import org.example.entity.Review;
import org.example.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/{placeId}")
    public ResponseEntity<List<Review>> getPlaceReviews(@PathVariable String placeId){
        return new ResponseEntity<>(reviewService.getReviewsByPlaceId(placeId), HttpStatus.OK);
    }
}
