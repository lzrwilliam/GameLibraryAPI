package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.service.RentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/Rents")
public class RentController {

    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @PostMapping("/Rent/game/{gameid}/user/{userid}/for/{length}")
    public ResponseEntity<?> rentGame(@PathVariable int gameid, 
                                         @PathVariable int userid, 
                                         @PathVariable int length) {
        if (gameid <= 0 || userid <= 0 || length <= 0) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            Rent rent = rentService.rentGame(gameid, userid, length);
            return ResponseEntity.ok(rent);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
    

    @PatchMapping("/Extend/game/{gameid}/user/{userid}/start/{startDate}/for/{length}")
    public ResponseEntity<?> extendRent(@PathVariable int gameid, @PathVariable int userid, 
                                        @PathVariable String startDate, @PathVariable int length) {
        try {
            LocalDate date = LocalDate.parse(startDate);
            Rent updatedRent = rentService.extendRent(gameid, userid, date, length);
            return ResponseEntity.ok(updatedRent);
        } catch (DateTimeParseException e) {
            return ResponseEntity.status(400).body("Invalid date format: " + startDate);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Returnează mesajul de eroare
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage());
        }
    }
    

    @DeleteMapping("/DeleteAll")
    public ResponseEntity<String> deleteAllRents() {
        rentService.deleteAllRents();
        return ResponseEntity.ok("All rents deleted successfully");
    }

    @GetMapping("/GetAll")
    public ResponseEntity<List<Rent>> getAllRents() {
        return ResponseEntity.ok(rentService.getAllRents());
    }
}
