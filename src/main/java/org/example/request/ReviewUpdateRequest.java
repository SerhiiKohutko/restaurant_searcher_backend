package org.example.request;

import lombok.Getter;

@Getter
public class ReviewUpdateRequest {
    private Long reviewId;
    private String message;
    private Double rating;
}
