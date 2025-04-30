package org.example.integration;

import jakarta.transaction.Transactional;
import org.example.entity.User;
import org.example.jwt.JwtProvider;
import org.example.service.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class DeleteFavouritePlaceIntegration {


    @Container
    private static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        mysql.start();
    }

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Captor
    private ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    private String jwtToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test@mail.com");
        user.setPassword("encodedPassword");
        user.setFavouritePlaces(new ArrayList<>(List.of("ChIJJ_po9kbO1EARmnEz9Lf7OoY")));

        userRepository.save(user);

        jwtToken = jwtProvider.generateToken(user);
    }


    @Test
    void testDeletePlaceById() throws Exception {
        String placeId = "ChIJJ_po9kbO1EARmnEz9Lf7OoY";

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/delete_place")
                .param("place_id", placeId)
                .header("Authorization", "Bearer " + jwtToken));


        User savedUser = userRepository.findUserByUsername("test@mail.com").orElseThrow();

        Assertions.assertNotNull(savedUser);
        Assertions.assertTrue(savedUser.getFavouritePlaces().isEmpty());
    }


}
