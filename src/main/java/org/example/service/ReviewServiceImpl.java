package org.example.service;

import org.example.entity.Place;
import org.example.entity.Review;
import org.example.entity.User;
import org.example.request.ReviewCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private PlacesService placesService;
    @Autowired
    private UserService userService;

    @Override
    public List<Review> getReviewsByPlaceId(String placeId) {
        placesService.isPlaceIdValid(placeId);
        return reviewRepository.findAllReviewsForPlace(placeId);
    }

    @Override
    public Review createReview(ReviewCreationRequest request) {
        String placeId = request.getPlaceId();

        placesService.isPlaceIdValid(placeId);

        User currentUser =
                userService.getUserFromSecurityContext();

        Review review = new Review();
        review.setDateCreated(new Date());
        review.setMessage(request.getMessage());
        review.setRating(request.getRating());
        review.setUser(currentUser);

        Optional<Place> optionalPlace
                = placesService.getPlaceById(request.getPlaceId());

        if (optionalPlace.isPresent()) {
           review.setPlace(optionalPlace.get());
           return reviewRepository.save(review);
        }

        Place place = new Place();
        place.setPlaceId(request.getPlaceId());

        review.setPlace(place);

        return reviewRepository.save(review);
    }
}
