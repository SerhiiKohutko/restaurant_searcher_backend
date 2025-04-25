package org.example.dto;

import lombok.Data;

@Data
public class PlaceMarker {
    private String placeId;
    private String type;
    private double lat;
    private double lng;
}
