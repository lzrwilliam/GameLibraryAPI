package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;

import ro.unibuc.hello.service.GameService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        Optional<Game> game = _gameService.getGameById(id);
        return game.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/Add")
    public Game addGame(@RequestBody Game game) {
        return _gameService.addGame(game);
    }

    @PatchMapping("/Rent/game={gameid}&user={userid}&for={length}")
    public Rent rentGame(@PathVariable String gameid, @PathVariable String userid, @PathVariable int length){
        return _gameService.rentGame(gameid, userid, length);
    }



    @DeleteMapping("Delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        _gameService.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/DeleteAll")
    public ResponseEntity<String> deleteAllUsers() {
        _gameService.deleteAllGames();
        return ResponseEntity.ok("Toate jocurile au fost ștersee");
    }
}
