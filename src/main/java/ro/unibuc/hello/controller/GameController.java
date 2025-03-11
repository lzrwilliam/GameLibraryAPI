package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.Review;
import ro.unibuc.hello.dto.ReviewRequest;

import ro.unibuc.hello.service.GameService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService _gameService;


    public GameController(GameService gameService) {
        this._gameService = gameService;
    }

    @GetMapping("/GetAll")
    public List<Game> getAllGames() {
        return _gameService.getAllGames();
    }

    @GetMapping("/Find/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id) {
        Optional<Game> game = _gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/Add")
    public Game addGame(@RequestBody Game game) {
        return _gameService.addGame(game);
    }

    @PatchMapping("/Rent/game={gameid}&user={userid}&for={length}")
    public Rent rentGame(@PathVariable int gameid, @PathVariable int userid, @PathVariable int length){
        return _gameService.rentGame(gameid, userid, length);
    }

    @PatchMapping("/Extend/game/{gameid}/user/{userid}/start/{startDate}/for/{length}")
    public ResponseEntity<?> extendRent(@PathVariable int gameid, @PathVariable int userid, @PathVariable String startDate, @PathVariable int length ){
        try {
            LocalDate dateStartDate = LocalDate.parse(startDate);
            Rent rent = _gameService.extendRent(gameid, userid, dateStartDate, length);
            return ResponseEntity.ok(rent);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("Invalid date format: " + startDate);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/DeleteAllRents")
    public ResponseEntity<String> deleteAllRents(){
        _gameService.deleteAllrents();
        return ResponseEntity.ok("Toate inchirierile au fost ștersee");
    }


    @DeleteMapping("/Delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable  int id) {
        _gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/DeleteAllGames")
    public ResponseEntity<String> deleteAllGames() {
        _gameService.deleteAllGames();
        return ResponseEntity.ok("Toate jocurile au fost ștersee");
    }


@PostMapping("/AddReview")
public ResponseEntity<String> addReview(@RequestBody ReviewRequest reviewRequest) {
    try {
        int gameId = Integer.parseInt(reviewRequest.getGameId());
        int userId = Integer.parseInt( reviewRequest.getUserId());

        return ResponseEntity.ok(_gameService.addReview(
                userId,
                gameId,
                reviewRequest.getReviewText(),
                reviewRequest.getRating()
        ));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


    @GetMapping("/{gameId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable int gameId) {
        return ResponseEntity.ok(_gameService.getReviewsForGame(gameId));
    }
}
