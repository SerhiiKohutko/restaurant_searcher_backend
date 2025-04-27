package org.example.service;

import org.example.dto.PlaceDto;
import org.example.entity.User;
import org.example.exceptions.InvalidPlaceIdException;
import org.example.exceptions.NoUserFoundException;
import org.example.exceptions.UsernameAlreadyInUseException;
import org.example.jwt.JwtProvider;
import org.example.response.AddFavouritePlaceResponse;
import org.example.response.DeletePlaceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.CredentialException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private GoogleMapService googleMapService;

    @Override
    public void registerUser(String username, String password) {
        if (userRepository.findUserByUsername(username).isPresent()){
            throw new UsernameAlreadyInUseException();
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));

        userRepository.save(newUser);
    }

    @Override
    public String authenticate(String username, String password) throws CredentialException {
        Optional<User> user = userRepository.findUserByUsername(username);

        if (user.isEmpty()){
            throw new NoUserFoundException(username);
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            throw new CredentialException("Wrong password");
        }

        return jwtProvider.generateToken(user.get());
    }

    @Override
    public List<String> getFavouritePlaces() {

        User obtainedUser = getUserFromSecurityContext();

        return obtainedUser.getFavouritePlaces();
    }

    @Override
    public AddFavouritePlaceResponse addFavouritePlaceById(String placeId) {

        //throws exception if place details null
        isPlaceIdValid(placeId);

        User obtainedUser = getUserFromSecurityContext();
        obtainedUser.getFavouritePlaces().add(placeId);
        userRepository.save(obtainedUser);

        return new AddFavouritePlaceResponse();
    }

    private void isPlaceIdValid(String placeId){
        PlaceDto placeDto = googleMapService.getPlaceDetailsFromGoogleMaps(placeId);
        if (placeDto.getAddress() == null || placeDto.getName() == null){
            throw new InvalidPlaceIdException(placeId);
        }
    }

    @Override
    public DeletePlaceResponse deletePlaceById(String placeId) {
        isPlaceIdValid(placeId);

        User obtainedUser = getUserFromSecurityContext();
        obtainedUser.getFavouritePlaces().remove(placeId);
        userRepository.save(obtainedUser);

        return new DeletePlaceResponse();
    }


    private User getUserFromSecurityContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isEmpty()) {
            throw new RuntimeException("Something went wrong while processing user data");
        }

        return user.get();
    }
}
