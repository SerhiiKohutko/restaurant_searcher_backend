package org.example.service;

import org.example.dto.PlaceDto;
import org.example.entity.User;
import org.example.exceptions.InvalidPlaceIdException;
import org.example.GoogleMapsApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes = GoogleMapsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserPlacesTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private GoogleMapService googleMapService;


    @Captor
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

    private User user = new User();
    private List<String> favouritePlaces = new ArrayList<>(List.of("1", "2", "3"));
    private String username;

    @BeforeEach
    void setUp(){
        Authentication authentication =
                Mockito.mock(Authentication.class);

        SecurityContext securityContext =
                Mockito.mock(SecurityContext.class);

        username = "test";

        user.setUsername(username);
        user.setPassword("test");
        user.setFavouritePlaces(favouritePlaces);

        Mockito.when(authentication.getName())
                .thenReturn(user.getUsername());
        Mockito.when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    @DisplayName("Get User Favourite Places")
    void testGetFavouritePlaces_whenCalled_returnValidList(){

        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));

        Assertions.assertEquals(favouritePlaces, userService.getFavouritePlaces());
    }

    @Test
    @DisplayName("Add Favourite Place By Id")
    void testAddFavouritePlaceById_whenCorrectIdProvided_returnAuthResponse(){
        String placeId = "4";

        String placeName = "test";
        String placeAddress = "test_address";
        List<String> expectedList = new ArrayList<>(favouritePlaces);
        expectedList.add(placeId);

        PlaceDto placeDto = new PlaceDto();
        placeDto.setName(placeName);
        placeDto.setAddress(placeAddress);

        User savedUser = new User();
        savedUser.setUsername(username);
        savedUser.setFavouritePlaces(expectedList);

        Mockito.when(googleMapService.getPlaceDetailsFromGoogleMaps(placeId))
                .thenReturn(placeDto);
        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user))
                        .thenReturn(savedUser);

        User updatedUser =
                userService.addFavouritePlaceById(placeId);

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(updatedUser.getFavouritePlaces(), expectedList);

    }

    @Test
    @DisplayName("Add Favourite Place By Id With Non Existing Id")
    void testAddFavouritePlaceById_whenInvalidIdProvided_throwInvalidPlaceIdException(){
        String placeId = "4";
        PlaceDto placeDto = new PlaceDto();

        Mockito.when(googleMapService.getPlaceDetailsFromGoogleMaps(placeId))
                .thenReturn(placeDto);


        Assertions.assertThrows(InvalidPlaceIdException.class, () -> userService.addFavouritePlaceById(placeId));

    }


    @Test
    @DisplayName("Delete Favourite Place From List")
    void testDeletePlaceById_whenValidIdProvided_returnResponse(){

        String placeId = "3";

        String placeName = "test";
        String placeAddress = "test_address";
        List<String> expectedList = new ArrayList<>(favouritePlaces);
        expectedList.remove(placeId);

        PlaceDto placeDto = new PlaceDto();
        placeDto.setName(placeName);
        placeDto.setAddress(placeAddress);

        User updatedUser = new User();
        updatedUser.setFavouritePlaces(expectedList);

        Mockito.when(googleMapService.getPlaceDetailsFromGoogleMaps(placeId))
                .thenReturn(placeDto);

        Mockito.when(userRepository.findUserByUsername(username))
                        .thenReturn(Optional.of(user));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                        .thenReturn(updatedUser);

        userService.deletePlaceById(placeId);

        Mockito.verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        Assertions.assertEquals(savedUser.getFavouritePlaces(), expectedList);

    }
}
