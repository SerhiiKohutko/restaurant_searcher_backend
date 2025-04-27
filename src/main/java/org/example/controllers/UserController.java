package org.example.controllers;

import org.example.response.AddFavouritePlaceResponse;
import org.example.response.DeletePlaceResponse;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PatchMapping("/add_favourite_place")
    public ResponseEntity<AddFavouritePlaceResponse> addFavouritePlaceToUserList(@RequestParam("place_id") String placeId){
        return new ResponseEntity<>(userService.addFavouritePlaceById(placeId), HttpStatus.ACCEPTED);
    }

    @GetMapping("/favourite_places")
    public ResponseEntity<List<String>> getUserFavouritePlaces(){
        return ResponseEntity.ok(userService.getFavouritePlaces());
    }

    @DeleteMapping("/delete_place")
    public ResponseEntity<DeletePlaceResponse> deletePlaceFromUserList(@RequestParam("place_id") String placeId){
        return new ResponseEntity<>(userService.deletePlaceById(placeId), HttpStatus.ACCEPTED);
    }

}
