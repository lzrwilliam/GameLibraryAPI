package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Review;
import ro.unibuc.hello.data.model.AgeCategory;
import ro.unibuc.hello.dto.ReviewRequest;
import ro.unibuc.hello.service.GameService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class GameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private Game testGame;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
        testGame = new Game(1, "Test Game", "Action", 59.99, 10, 10, 4, LocalDate.now(), 30.0, AgeCategory.TEENS);
    }

    @Test
    void testGetAllGamesByGenre() throws Exception {
        when(gameService.getAllGamesByGenre("Action")).thenReturn(Arrays.asList(testGame));

        mockMvc.perform(get("/games/genre/Action"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    void testGetAllGames() throws Exception {
        when(gameService.getAllGames()).thenReturn(Arrays.asList(testGame));

        mockMvc.perform(get("/games/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Game"));
    }

    @Test
    void testGetGameById_Found() throws Exception {
        when(gameService.getGameById(1)).thenReturn(Optional.of(testGame));

        mockMvc.perform(get("/games/Find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void testGetGameById_NotFound() throws Exception {
        when(gameService.getGameById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/games/Find/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddGame() throws Exception {
        when(gameService.addGame(any(Game.class))).thenReturn(testGame);

        mockMvc.perform(post("/games/Add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testGame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Game"));
    }

    @Test
    void testDeleteGame() throws Exception {
        // Se apelează endpoint-ul de delete; nu este returnat niciun body.
        mockMvc.perform(delete("/games/Delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteAllGames() throws Exception {
        mockMvc.perform(delete("/games/DeleteAllGames"))
                .andExpect(status().isOk())
                .andExpect(content().string("Toate jocurile au fost sterse"));
    }

    @Test
    void testAddReview_Success() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setUserId("1");
        reviewRequest.setGameId("1");
        reviewRequest.setReviewText("Awesome game!");
        reviewRequest.setRating(5);

        when(gameService.addReview(1, 1, "Awesome game!", 5))
                .thenReturn("Review adaugat cu succes!");

        mockMvc.perform(post("/games/AddReview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Review adaugat cu succes!"));
    }

    @Test
    void testAddReview_Failure() throws Exception {
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setUserId("1");
        reviewRequest.setGameId("1");
        reviewRequest.setReviewText("Awesome game!");
        reviewRequest.setRating(5);

        when(gameService.addReview(1, 1, "Awesome game!", 5))
                .thenThrow(new RuntimeException("Error adding review"));

        mockMvc.perform(post("/games/AddReview")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error adding review"));
    }

    @Test
    void testGetReviewsForGame() throws Exception {
        Review review = new Review(1, "Great game!", 5);
        List<Review> reviews = Arrays.asList(review);
        when(gameService.getReviewsForGame(1)).thenReturn(reviews);

        mockMvc.perform(get("/games/1/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reviewText").value("Great game!"));
    }
}
