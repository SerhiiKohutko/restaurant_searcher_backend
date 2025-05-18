package org.example.service;

import org.example.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(value = "select * from reviews r where r.place_id = :place_id order by date_created desc", nativeQuery = true)
    List<Review> findAllReviewsForPlace(@Param("place_id") String placeId);

    Optional<Review> findById(Long id);
}
