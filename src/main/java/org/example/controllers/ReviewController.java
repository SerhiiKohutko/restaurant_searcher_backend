package org.example.controllers;

import org.example.entity.Review;
import org.example.request.ReviewCreationRequest;
import org.example.request.ReviewUpdateRequest;
import org.example.response.ReviewDeletionResponse;
import org.example.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewCreationRequest request){
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDeletionResponse> deleteReview(@PathVariable Long reviewId){
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.ok(new ReviewDeletionResponse(reviewId));
    }

    @PatchMapping("/update_review")
    public ResponseEntity<Review> updateReview(@RequestBody ReviewUpdateRequest request){
        return ResponseEntity.ok(reviewService.updateReview(request));
    }
}
