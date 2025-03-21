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

    @GetMapping("/genre/{genre}")
    public List<Game> getAllGamesByGenre(@PathVariable String genre) {
        return _gameService.getAllGamesByGenre(genre);
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
