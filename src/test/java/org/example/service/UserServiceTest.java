package org.example.service;

import org.example.entity.User;
import org.example.exceptions.NoUserFoundException;
import org.example.exceptions.UsernameAlreadyInUseException;
import org.example.GoogleMapsApplication;
import org.example.jwt.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import javax.security.auth.login.CredentialException;
import java.util.Optional;

@SpringBootTest(classes = GoogleMapsApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private PasswordEncoder passwordEncoder;
    @MockitoBean
    private JwtProvider jwtProvider;
    @MockitoBean
    private GoogleMapService googleMapService;

    private String username;
    private String password;
    private String encodedPassword;

    @BeforeEach
    void setUp(){
         username = "test_user";
         password = "test_password";
         encodedPassword = "test_encoded_password";
    }
    @Test
    @DisplayName("User Registration")
    void testRegisterUser_whenValidDataProvided_NoExceptionThrown(){
        //Arrange
        String username = "test";
        String password = "test";

        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("test");
        //Action

        Assertions.assertDoesNotThrow(() -> userService.registerUser(username, password));

        //Assert

        Mockito.verify(userRepository, Mockito.times(1))
                .save(Mockito.argThat(user -> user.getUsername().equals(username) && user.getPassword().equals("test")));
    }

    @Test
    @DisplayName("Register User Throws Exception")
    void testCreateUser_whenUsedUsernameProvided_throwException(){
        String username = "test";
        String password = "test";

        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(new User()));

        Assertions.assertThrows(
                UsernameAlreadyInUseException.class,
                () -> userService.registerUser(username, password)
        );
    }

    @Test
    @DisplayName("Authenticate user with valid credentials")
    void testAuthenticate_whenValidCredentialsProvided_returnJwt() throws CredentialException {
        User user = new User();
        String expectedToken = "jwt_expected";

        user.setUsername(username);
        user.setPassword(password);

        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));

        Mockito.when(passwordEncoder.matches(password, user.getPassword()))
                .thenReturn(true);

        Mockito.when(jwtProvider.generateToken(user))
                .thenReturn(expectedToken);

        String actualToken = userService.authenticate(username, password);

        Assertions.assertEquals(expectedToken, actualToken);
        
    }

    @Test
    @DisplayName("Username not found while authnecticate")
    void testAuthenticate_whenNotRegisteredUsernameProvided_throwNoUserFoundException(){
        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(NoUserFoundException.class, () -> userService.authenticate(username, password));
    }

    @Test
    @DisplayName("Invalid password test")
    void testAuthenticate_whenInvalidPasswordProvided_throwBadCredentialsException(){

        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        Mockito.when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(password, user.getPassword()))
                .thenReturn(false);

        Assertions.assertThrows(CredentialException.class, () -> userService.authenticate(username, password));
    }

}
