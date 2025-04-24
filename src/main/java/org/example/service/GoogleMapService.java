package org.example.service;

import org.example.dto.PlaceDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GoogleMapService {
    private static final String GOOGLE_MAPS_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private static final String GOOGLE_MAPS_PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo";

    private final String apiKey = "AIzaSyCtyCHoLK5orbeDrsODnVTuWqwad0be3eQ";


    public PlaceDto getPlaceDetailsFromGoogleMaps(String id) {
        RestTemplate restTemplate = new RestTemplate();
        String url = UriComponentsBuilder.fromUriString(GOOGLE_MAPS_URL)
                .queryParam("place_id", id)
                .queryParam("key", apiKey)
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        RequestEntity<Void> request = new RequestEntity<>(headers, HttpMethod.GET, URI.create(url));

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                request,
                new ParameterizedTypeReference<>() {
                }
        );

        Map<String, Object> responseBody = response.getBody();

        return mapToPlaceDto(responseBody);
    }

    private PlaceDto mapToPlaceDto(Map<String, Object> responseBody) {
        PlaceDto placeDto = new PlaceDto();

        if (responseBody != null && responseBody.containsKey("result")) {
            Map<String, Object> result = (Map<String, Object>) responseBody.get("result");

            // Map name
            if (result.containsKey("name")) {
                placeDto.setName((String) result.get("name"));
            }

            // Map type (from the first type in the list)
            if (result.containsKey("types") && ((List<String>) result.get("types")).size() > 0) {
                String type = ((List<String>) result.get("types")).get(0);
                placeDto.setType(formatType(type));
            }

            // Map short description (using rating and number of reviews)
            if (result.containsKey("rating") && result.containsKey("user_ratings_total")) {
                double rating = (double) result.get("rating");
                int reviewCount = (int) result.get("user_ratings_total");
                placeDto.setDescription_short(String.format("Рейтинг %.1f на основе %d отзывов", rating, reviewCount));
            }

            // Map address
            if (result.containsKey("formatted_address")) {
                placeDto.setAddress((String) result.get("formatted_address"));
            }

            // Map location
            if (result.containsKey("geometry") && ((Map<String, Object>) result.get("geometry")).containsKey("location")) {
                Map<String, Object> location = (Map<String, Object>) ((Map<String, Object>) result.get("geometry")).get("location");
                Map<String, Double> locationMap = new HashMap<>();
                locationMap.put("latitude", (Double) location.get("lat"));
                locationMap.put("longitude", (Double) location.get("lng"));
                placeDto.setLocation(locationMap);
            }

            // Map opening hours
            if (result.containsKey("opening_hours") && ((Map<String, Object>) result.get("opening_hours")).containsKey("weekday_text")) {
                List<String> weekdayText = (List<String>) ((Map<String, Object>) result.get("opening_hours")).get("weekday_text");
                placeDto.setOpening_hours(String.join(", ", weekdayText));
            }

            // Map phone
            if (result.containsKey("formatted_phone_number")) {
                placeDto.setPhone((String) result.get("formatted_phone_number"));
            }

            // Map website
            if (result.containsKey("website")) {
                placeDto.setWebsite((String) result.get("website"));
            }

            // Map image URL (using the first photo reference)
            if (result.containsKey("photos") && ((List<Map<String, Object>>) result.get("photos")).size() > 0) {
                Map<String, Object> firstPhoto = ((List<Map<String, Object>>) result.get("photos")).get(0);
                if (firstPhoto.containsKey("photo_reference")) {
                    String photoRef = (String) firstPhoto.get("photo_reference");
                    String photoUrl = buildPhotoUrl(photoRef);
                    placeDto.setImage_url(photoUrl);
                }
            }
        }

        return placeDto;
    }

    private String formatType(String type) {
        return switch (type) {
            case "restaurant" -> "Ресторан";
            case "cafe" -> "Кафе";
            case "bar" -> "Бар";
            case "night_club" -> "Клуб";
            case "bakery" -> "Булочная";
            default -> "Заведение";
        };
    }

    private String buildPhotoUrl(String photoReference) {
        return UriComponentsBuilder.fromUriString(GOOGLE_MAPS_PHOTO_URL)
                .queryParam("photoreference", photoReference)
                .queryParam("maxwidth", 400)
                .queryParam("key", apiKey)
                .toUriString();
    }
}
