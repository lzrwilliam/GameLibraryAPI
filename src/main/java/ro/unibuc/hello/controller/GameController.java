package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/GetAll")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/Find/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Optional<Game> game = gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/Add")
    public Game addGame(@RequestBody Game game) {
        return gameService.addGame(game);
    }

    @DeleteMapping("Delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/DeleteAll")
    public ResponseEntity<String> deleteAllUsers() {
        gameService.deleteAllGames();
        return ResponseEntity.ok("Toate jocurile au fost ștersee");
    }
}
