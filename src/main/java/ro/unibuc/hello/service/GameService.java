package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;

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

    public GameService(GameRepository gameRepository, RentRepository rentRepository, UserRepository userRepository) {
        this._gameRepository = gameRepository;
        this._rentRepository = rentRepository;
        this._userRepository = userRepository;
    }

    public List<Game> getAllGames() {
        return _gameRepository.findAll();
    }

    public Optional<Game> getGameById(String id) {
        return _gameRepository.findById(id);
    }

    public Game addGame(Game game) {
        return _gameRepository.save(game);
    }

    public Rent rentGame(String gameID, String userID, int length) {
        
        Optional<Game> optionalGame = getGameById(gameID);
        Optional<User> optionalUser = _userRepository.findById(userID);

        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Game not found with ID: " + gameID);
        }

        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found with ID: " + userID);
        }
    
        Game game = optionalGame.get(); 
        int noCopies = game.getAvailableCopies();
        if (noCopies >= 1) {
            game.setAvailableCopies(game.getAvailableCopies() - 1);
            _gameRepository.save(game);
            double price = game.getPrice();
            price *= length;
            return _rentRepository.save(new Rent(userID, gameID, LocalDate.now(), length, price));
        }
    
        throw new RuntimeException("No available copies for game: " + gameID);
    }
    

    public void deleteGame(String id) {
        _gameRepository.deleteById(id);
    }

    public void deleteAllGames(){
        _gameRepository.deleteAll();
    }
}
