package org.example.integration;

import jakarta.transaction.Transactional;
import org.example.dto.PlaceDto;
import org.example.entity.User;
import org.example.jwt.JwtProvider;
import org.example.service.GoogleMapService;
import org.example.service.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class AddFavouritePlaceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        postgres.start();
    }
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // These properties are now set after the container is guaranteed to be started
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        registry.add("spring.datasource.embedded-database-connection", () -> "none");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private GoogleMapService googleMapService;

    @Autowired
    private JwtProvider jwtProvider;

    private String jwtToken;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test@mail.com");
        user.setPassword("encodedPassword");
        user.setFavouritePlaces(new ArrayList<>());

        userRepository.save(user);

        jwtToken = jwtProvider.generateToken(user);
    }

    @Test
    void testAddFavouritePlace() throws Exception {
        // Use the same place ID in both the request and assertion
        String placeId = "test_id";
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("test_place");
        placeDto.setAddress("test_address");

        Mockito.when(googleMapService.getPlaceDetailsFromGoogleMaps(Mockito.any(String.class)))
                        .thenReturn(placeDto);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/user/add_favourite_place")
                        .param("place_id", placeId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isAccepted());

        User updated = userRepository.findUserByUsername("test@mail.com").orElseThrow();
        Assertions.assertTrue(updated.getFavouritePlaces().contains(placeId));
    }
}