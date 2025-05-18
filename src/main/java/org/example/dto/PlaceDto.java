package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Review;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
    private String name;
    private String type;
    private String description_short;
    private String address;
    private Map<String, Double> location;
    private String opening_hours;
    private String phone;
    private String website;
    private String image_url;
    private List<Review> reviews;
}