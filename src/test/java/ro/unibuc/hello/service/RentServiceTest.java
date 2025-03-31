package ro.unibuc.hello.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.repository.RentRepository;
import ro.unibuc.hello.data.repository.UserRepository;

import ro.unibuc.hello.data.model.AgeCategory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class RentServiceTest {
    @InjectMocks
    private RentService rentService;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    private Game game;
    private User user;
    private Rent rent;
    private final int userID = 1;
    private final int gameID = 1;
    private final LocalDate startDate = LocalDate.now();

     @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        game = new Game(gameID, "Cyberpunk 2078", "RPG", 2.99, 50, 50, 1, LocalDate.of(2024, 3, 4), 59.99, AgeCategory.MATURE);
        user = new User(userID, "John", "Doe", "johndoe", "securepassword123", LocalDate.of(1990, 1, 1), 10.00, "johndoe@example.com", "1234567890");
        rent = new Rent(userID, gameID, startDate, 3, 8.97);
    }

    @Test
    void testGetRent_Success() {
        when(rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)).thenReturn(Optional.of(rent));
        Rent result = rentService.getRent(userID, gameID, startDate);
        assertNotNull(result);
        assertEquals(userID, result.getUserID());
        assertEquals(gameID, result.getGameID());
    }
    
    @Test
    void testGetRent_NotFound() {
        when(rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> rentService.getRent(userID, gameID, startDate));
    }
    @Test
    void testRentGame_Success() {
        when(gameRepository.findById(gameID)).thenReturn(Optional.of(game));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        when(rentRepository.findAll()).thenReturn(List.of());
        when(rentRepository.save(any(Rent.class))).thenReturn(rent);

        Rent result = rentService.rentGame(gameID, userID, 3);
        assertNotNull(result);
        assertEquals(userID, result.getUserID());
        assertEquals(gameID, result.getGameID());
    }

    @Test
    void testRentGame_InsufficientBalance() {
        when(gameRepository.findById(gameID)).thenReturn(Optional.of(game));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> rentService.rentGame(gameID, userID, 4));
    }

    @Test
    void testRentGame_NoAvailableCopies() {
        game.setAvailableCopies(0);
        when(gameRepository.findById(gameID)).thenReturn(Optional.of(game));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> rentService.rentGame(gameID, userID, 3));
    }

    @Test
    void testExtendRent_Success() {
        when(rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)).thenReturn(Optional.of(rent));
        when(gameRepository.findById(gameID)).thenReturn(Optional.of(game));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        when(rentRepository.save(any(Rent.class))).thenReturn(rent);

        Rent result = rentService.extendRent(gameID, userID, startDate, 1);
        assertNotNull(result);
        assertEquals(userID, result.getUserID());
        assertEquals(gameID, result.getGameID());
    }

    @Test
    void testExtendRent_InsufficientFunds() {
        user.setBalance(1.00);
        when(rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)).thenReturn(Optional.of(rent));
        when(gameRepository.findById(gameID)).thenReturn(Optional.of(game));
        when(userRepository.findById(userID)).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> rentService.extendRent(gameID, userID, startDate, 2));
    }

    @Test
    void testExtendRent_NotFound() {
        when(rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> rentService.extendRent(gameID, userID, startDate, 1));
    }

    @Test
    void testDeleteAllRents() {
        doNothing().when(rentRepository).deleteAll();
        assertDoesNotThrow(() -> rentService.deleteAllRents());
        verify(rentRepository, times(1)).deleteAll();
    }

    @Test
    void testGetAllRents() {
        when(rentRepository.findAll()).thenReturn(List.of(rent));
        List<Rent> result = rentService.getAllRents();
        assertEquals(1, result.size());
    }

}