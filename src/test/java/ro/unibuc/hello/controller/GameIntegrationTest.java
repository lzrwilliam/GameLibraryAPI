/*package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ro.unibuc.hello.data.model.AgeCategory;
import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.dto.ReviewRequest;
import ro.unibuc.hello.service.GameService;
import ro.unibuc.hello.data.repository.RentRepository;
import ro.unibuc.hello.data.repository.UserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GameIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017)
            .withSharding();

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        String uri = String.format("mongodb://localhost:%d/testdb", mongoDBContainer.getMappedPort(27017));
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final Game testGame = new Game(
            0, "Integration Game", "RPG", 10.0,
            10, 10, 4, LocalDate.of(2024, 3, 20),
            50.0, AgeCategory.TEENS
    );

    @Test
    @Order(1)
    void testAddGame() throws Exception {
        gameService.addGame(testGame);

        mockMvc.perform(get("/games/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Game"));
    }

    @Test
    @Order(2)
    void testGetAllGames() throws Exception {
        mockMvc.perform(get("/games/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Integration Game"));
    }

    @Test
    @Order(3)
    void testGetGameById() throws Exception {
        int id = gameService.getAllGames().get(0).getId();
        mockMvc.perform(get("/games/Find/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Game"));
    }

    @Test
    @Order(4)
    void testGetGamesByGenre() throws Exception {
        mockMvc.perform(get("/games/genre/RPG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("RPG"));
    }

    @Test
    @Order(5)
    void testAddValidReview() throws Exception {
        // Simulez user + rent
        User user = new User(999, "John", "Doe", "johndoe", "password123",
        LocalDate.of(2000, 5, 10), 100.0, "john.doe@example.com", "123456789");
        userRepository.save(user);

        Rent rent = new Rent(999, testGame.getId(), LocalDate.now(), 2,9);
        rentRepository.save(rent);

        ReviewRequest review = new ReviewRequest("999", String.valueOf(testGame.getId()), "Review text", 4);

        mockMvc.perform(post("/games/AddReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review adaugat cu succes!"));
    }

    @Test
    @Order(6)
    void testAddInvalidReview() throws Exception {
        ReviewRequest review = new ReviewRequest("1234", "4321", "test", 5);
        mockMvc.perform(post("/games/AddReview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(7)
    void testDeleteGame() throws Exception {
        int id = gameService.getAllGames().get(0).getId();
        mockMvc.perform(delete("/games/Delete/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    @Order(8)
    void testDeleteAllGames() throws Exception {
        gameService.addGame(new Game(0, "G1", "FPS", 10, 10, 10, 2, LocalDate.now(), 30, AgeCategory.TEENS));
        gameService.addGame(new Game(0, "G2", "Strategy", 20, 10, 10, 4, LocalDate.now(), 45, AgeCategory.TEENS));

        mockMvc.perform(delete("/games/DeleteAllGames"))
                .andExpect(status().isOk())
                .andExpect(content().string("Toate jocurile au fost sterse"));
    }
}
*/
