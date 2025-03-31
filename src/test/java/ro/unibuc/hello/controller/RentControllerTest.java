package ro.unibuc.hello.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.service.RentService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class RentControllerTest {

    @InjectMocks
    private RentController rentController;

    @Mock
    private RentService rentService;

    private Rent rent;
    private final int userID = 1;
    private final int gameID = 1;
    private final LocalDate startDate = LocalDate.now();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rent = new Rent(userID, gameID, startDate, 3, 8.97);
    }

    @Test
    void testRentGame_Success() {
        when(rentService.rentGame(gameID, userID, 3)).thenReturn(rent);
        ResponseEntity<?> response = rentController.rentGame(gameID, userID, 3);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(rent, response.getBody());
    }

    @Test
    void testRentGame_BadRequest() {
        ResponseEntity<?> response = rentController.rentGame(-1, userID, 3);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testRentGame_RuntimeException() {
        when(rentService.rentGame(gameID, userID, 3)).thenThrow(new RuntimeException("Game not found"));
        ResponseEntity<?> response = rentController.rentGame(gameID, userID, 3);
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Game not found", response.getBody());
    }

    @Test
    void testExtendRent_Success() {
        when(rentService.extendRent(gameID, userID, startDate, 2)).thenReturn(rent);
        ResponseEntity<?> response = rentController.extendRent(gameID, userID, startDate.toString(), 2);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(rent, response.getBody());
    }

    @Test
    void testExtendRent_InvalidDateFormat() {
        ResponseEntity<?> response = rentController.extendRent(gameID, userID, "invalid-date", 2);
        assertEquals(400, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Invalid date format"));
    }

    @Test
    void testExtendRent_NotFound() {
        when(rentService.extendRent(gameID, userID, startDate, 2)).thenThrow(new RuntimeException("No active rent found"));
        ResponseEntity<?> response = rentController.extendRent(gameID, userID, startDate.toString(), 2);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("No active rent found", response.getBody());
    }

    @Test
    void testDeleteAllRents() {
        ResponseEntity<String> response = rentController.deleteAllRents();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("All rents deleted successfully", response.getBody());
    }

    @Test
    void testGetAllRents() {
        when(rentService.getAllRents()).thenReturn(List.of(rent));
        ResponseEntity<List<Rent>> response = rentController.getAllRents();
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }
}
