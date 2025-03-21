package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.model.Review;

import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.repository.RentRepository;
import ro.unibuc.hello.data.repository.UserRepository;



import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GameService {

    private final GameRepository _gameRepository;
    private final RentRepository _rentRepository;
    private final UserRepository _userRepository;
    private final CounterService counterService; 

    public GameService(GameRepository gameRepository, RentRepository rentRepository, UserRepository userRepository, CounterService counterService) {
        this._gameRepository = gameRepository;
        this._rentRepository = rentRepository;
        this._userRepository = userRepository;
        this.counterService = counterService;
    }

    public List<Game> getAllGamesByGenre(String genre) {
        return _gameRepository.findByGenre(genre);
    }

  

    
   
    
    
    public List<Game> getAllGames() {
        List<Game> games = _gameRepository.findAll();
        return games != null ? games : List.of();
    }
    

    public Optional<Game> getGameById(int id) {
        return _gameRepository.findById(id);
    }

    public Game addGame(Game game) {
        game.setId(counterService.getNextSequence("games"));
        return _gameRepository.save(game);
    }

    public void deleteGame(int id) {
        _gameRepository.deleteById(id);
    }

    public void deleteAllGames() {
        _gameRepository.deleteAll();
        counterService.resetCounter("games");
    }

    

    public String addReview(int userId, int gameId, String reviewText, int rating) {
        Optional<Game> gameOpt = _gameRepository.findById(gameId);
        if (gameOpt.isEmpty()) {
            throw new RuntimeException("Jocul nu există!");
        }
        
        Game game = gameOpt.get();
    
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating-ul trebuie sa fie intre 1 si 5!");
        }
        if (reviewText.isEmpty()) {
            throw new RuntimeException("Review-ul nu poate fi gol!");
        }
    
        List<Rent> rentals = _rentRepository.findAll();
        boolean hasRentedGame = rentals.stream().anyMatch(r -> r.getUserID() == userId && r.getGameID() == gameId);
    
        if (!hasRentedGame) {
            throw new RuntimeException("Nu poți lăsa un review pentru un joc pe care nu l-ai închiriat!");
        }
    
        boolean alreadyReviewed = game.getReviews().stream().anyMatch(r -> r.getUserId() == userId);
        if (alreadyReviewed) {
            throw new RuntimeException("Ai lăsat deja un review pentru acest joc!");
        }
    
        game.addReview(new Review(userId, reviewText, rating));
        _gameRepository.save(game);
    
        return "Review adaugat cu succes!";
    }
    
    public List<Review> getReviewsForGame(int gameId) {
        return _gameRepository.findById(gameId).map(Game::getReviews).orElse(List.of());
    }
    
}
