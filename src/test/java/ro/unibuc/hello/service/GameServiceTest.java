package ro.unibuc.hello.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ro.unibuc.hello.data.model.AgeCategory;
import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.Review;
import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.repository.RentRepository;
import ro.unibuc.hello.data.repository.UserRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CounterService counterService;

    @InjectMocks
    private GameService gameService;

    private Game testGame;

    @BeforeEach
    void setUp() {
        // Inițializezi jocul de test
        testGame = new Game(
                0,                  // id (va fi setat din CounterService)
                "Test Game",        // titlu
                "Action",           // gen
                59.99,              // preț
                10,                 // totalCopies
                10,                 // availableCopies
                4,                  // maxPlayers
                LocalDate.now(),    // addedDate
                30.0,               // purchasePrice
                AgeCategory.TEENS   // categorie de vârstă
        );
    }

    @Test
    void testGetAllGamesByGenre() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findByGenre("Action")).thenReturn(games);

        List<Game> result = gameService.getAllGamesByGenre("Action");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Game", result.get(0).getTitle());
    }

    @Test
    void testGetAllGames() {
        List<Game> games = Collections.singletonList(testGame);
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = gameService.getAllGames();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetGameById_GameExists() {
        when(gameRepository.findById(1)).thenReturn(Optional.of(testGame));

        Optional<Game> result = gameService.getGameById(1);

        assertTrue(result.isPresent());
        assertEquals("Test Game", result.get().getTitle());
    }

    @Test
    void testGetGameById_GameNotFound() {
        when(gameRepository.findById(2)).thenReturn(Optional.empty());

        Optional<Game> result = gameService.getGameById(2);

        assertFalse(result.isPresent());
    }

    @Test
    void testAddGame() {
        when(counterService.getNextSequence("games")).thenReturn(1);
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Game newGame = new Game(
                0,
                "New Game",
                "Adventure",
                39.99,
                20,
                20,
                5,
                LocalDate.now(),
                20.0,
                AgeCategory.MATURE
        );

        Game result = gameService.addGame(newGame);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(gameRepository, times(1)).save(newGame);
    }

    @Test
    void testDeleteGame() {
        gameService.deleteGame(1);
        verify(gameRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteAllGames() {
        gameService.deleteAllGames();
        verify(gameRepository, times(1)).deleteAll();
        verify(counterService, times(1)).resetCounter("games");
    }

    @Test
    void testAddReview_Success() {
        when(gameRepository.findById(1)).thenReturn(Optional.of(testGame));

        // simulăm că userId=1 a închiriat gameId=1
        Rent rent = mock(Rent.class);
        when(rent.getUserID()).thenReturn(1);
        when(rent.getGameID()).thenReturn(1);
        when(rentRepository.findAll()).thenReturn(Collections.singletonList(rent));

        String result = gameService.addReview(1, 1, "Great game!", 5);

        assertEquals("Review adaugat cu succes!", result);
        verify(gameRepository, times(1)).save(testGame);
        assertEquals(1, testGame.getReviews().size());
        assertEquals("Great game!", testGame.getReviews().get(0).getReviewText());
    }

    @Test
    void testAddReview_GameNotFound() {
        when(gameRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                gameService.addReview(1, 1, "Review", 4)
        );
        assertEquals("Jocul nu există!", exception.getMessage());
    }

   

    @Test
    void testAddReview_UserNotRentedGame() {
        when(gameRepository.findById(1)).thenReturn(Optional.of(testGame));
        // nicio închiriere, deci userId=1 nu a închiriat jocul 1
        when(rentRepository.findAll()).thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                gameService.addReview(1, 1, "Nice game", 4)
        );
        assertEquals("Nu poți lăsa un review pentru un joc pe care nu l-ai închiriat!", exception.getMessage());
    }

    @Test
    void testAddReview_AlreadyReviewed() {
        // Adăugăm deja un review pentru userId=1
        testGame.addReview(new Review(1, "Existing review", 3));

        when(gameRepository.findById(1)).thenReturn(Optional.of(testGame));

        // simulăm că userId=1 a închiriat gameId=1
        Rent rent = mock(Rent.class);
        when(rent.getUserID()).thenReturn(1);
        when(rent.getGameID()).thenReturn(1);
        when(rentRepository.findAll()).thenReturn(Collections.singletonList(rent));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                gameService.addReview(1, 1, "Another review", 4)
        );
        assertEquals("Ai lăsat deja un review pentru acest joc!", exception.getMessage());
    }

    @Test
    void testGetReviewsForGame() {
        Review review = new Review(1, "Good", 4);
        testGame.addReview(review);
        when(gameRepository.findById(1)).thenReturn(Optional.of(testGame));

        List<Review> reviews = gameService.getReviewsForGame(1);

        assertNotNull(reviews);
        assertEquals(1, reviews.size());
        assertEquals("Good", reviews.get(0).getReviewText());
    }
}
