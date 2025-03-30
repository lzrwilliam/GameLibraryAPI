package ro.unibuc.hello.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.AgeCategory;
import ro.unibuc.hello.service.RentService;
import ro.unibuc.hello.service.UserService;
import ro.unibuc.hello.service.GameService;
import java.time.LocalDate;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RentService rentService;

    @Autowired
    private UserService userService;

    @Autowired
    private GameService gameService;

    @BeforeEach
    void setup() {
        // Clear previous test data
        rentService.deleteAllRents();

        // Create 4 users
        User user1 = new User(1, "Alice", "Smith", "alice_smith", "password1", 
            LocalDate.of(1995, 5, 12), 500.0, "alice@example.com", "123456789");
        User user2 = new User(2, "Bob", "Johnson", "bob_johnson", "password2", 
            LocalDate.of(1990, 8, 20), 75.0, "bob@example.com", "987654321");
        User user3 = new User(3, "Charlie", "Brown", "charlie_brown", "password3", 
            LocalDate.of(1985, 11, 5), 30.0, "charlie@example.com", "456789123");
        User user4 = new User(4, "Dana", "Davis", "dana_davis", "password4", 
            LocalDate.of(2000, 2, 15), 100.0, "dana@example.com", "789123456");

        // Save users (assuming you have a user repository or service for saving)
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);

        // Create 4 games
        Game game1 = new Game(1, "Chess", "Strategy", 10.0, 5, 5, 2, 
            LocalDate.of(2020, 1, 1), 50.0, AgeCategory.EVERYONE);
        Game game2 = new Game(2, "Monopoly", "Board", 15.0, 10, 10, 4, 
            LocalDate.of(2021, 6, 15), 75.0, AgeCategory.TEENS);
        Game game3 = new Game(3, "Call of Duty", "Action", 60.0, 3, 2, 10, 
            LocalDate.of(2022, 3, 10), 120.0, AgeCategory.MATURE);
        Game game4 = new Game(4, "Minecraft", "Sandbox", 30.0, 8, 8, 10, 
            LocalDate.of(2019, 11, 25), 90.0, AgeCategory.EVERYONE);

        // Save games (assuming you have a game repository or service for saving)
        gameService.save(game1);
        gameService.save(game2);
        gameService.save(game3);
        gameService.save(game4);

        // Create rent linked to specific users and games
        
        Rent rent2 = new Rent(2, 2, LocalDate.now(), 1, 15.0); // User 2 rents Game 2

        // Save rents
        rentService.save(rent2);
    }


    @Test
    void testRentGame_Success() throws Exception {
        mockMvc.perform(post("/Rents/Rent/game/1/user/1/for/7")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gameID").value(1))
                .andExpect(jsonPath("$.userID").value(1))
                .andExpect(jsonPath("$.startDate").isNotEmpty())
                .andExpect(jsonPath("$.endDate").isNotEmpty())
                .andExpect(jsonPath("$.price").value(70.0));
    }
    @Test
    void testRentGame_BadRequest() throws Exception {
        mockMvc.perform(post("/Rents/Rent/game/0/user/0/for/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testExtendRent_Success() throws Exception {
        Rent rent1 = new Rent(1, 1, LocalDate.now(), 1, 10.0); // User 1 rents Game 1
        rentService.save(rent1);
        mockMvc.perform(patch("/Rents/Extend/game/1/user/1/start/" + LocalDate.now() + "/for/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endDate").isNotEmpty())
                .andExpect(jsonPath("$.price").value(60.0)); // Assuming extended price calculation
    }

    @Test
    void testExtendRent_InvalidDate() throws Exception {
        mockMvc.perform(patch("/Rents/Extend/game/1/user/1/start/invalid-date/for/5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid date format: invalid-date"));
    }

    @Test
    void testDeleteAllRents() throws Exception {
        mockMvc.perform(delete("/Rents/DeleteAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("All rents deleted successfully"));
    }

    @Test
    void testGetAllRents() throws Exception {
        mockMvc.perform(get("/Rents/GetAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
