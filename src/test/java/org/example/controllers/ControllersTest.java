package org.example.controllers;

import org.example.configuration.SecurityConfig;
import org.example.dto.PlaceMarker;
import org.example.service.PlacesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlacesController.class)
@Import(SecurityConfig.class)
public class ControllersTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlacesService placesService;

    @BeforeEach
    void setup(){
        Mockito.when(placesService.getAllPlaces())
                .thenReturn(List.of(new PlaceMarker(), new PlaceMarker()));
    }

    @Test
    @DisplayName("getPlaces returns 200 and list of placeMarkers")
    void testGetPlaces_returnsListOfPlaces() throws Exception {

        List<PlaceMarker> list = new ArrayList<>(List.of(new PlaceMarker(), new PlaceMarker()));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/places"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [{"placeId":null,"type":null,"lat":0.0,"lng":0.0},{"placeId":null,"type":null,"lat":0.0,"lng":0.0}]
                 """));
    }
}
