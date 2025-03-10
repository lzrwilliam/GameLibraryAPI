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
        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Game not found with ID: " + gameID);
        }

        Optional<User> optionalUser = _userRepository.findById(userID);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found with ID: " + userID);
        }
    
        Game game = optionalGame.get();
        int noCopies = game.getAvailableCopies(); 
        User user = optionalUser.get();
        double balance = user.getBalance();
        if (noCopies >= 1) {
            double price = game.getPrice();
            price *= length;

            if(balance < price){
                throw new RuntimeException("User dose not have enough money");
            }

            game.setAvailableCopies(game.getAvailableCopies() - 1);
            _gameRepository.save(game);
            user.addToBalance(-price);
            _userRepository.save(user);
            return _rentRepository.save(new Rent(userID, gameID, LocalDate.now(), length, price));
            
        }
    
        throw new RuntimeException("No available copies for game: " + gameID);
    }
    
    public Rent extendRent(String gameID, String userID, LocalDate startDate, int length){
        Optional<Game> optionalGame = getGameById(gameID);
        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Game not found with ID: " + gameID);
        }

        Optional<User> optionalUser = _userRepository.findById(userID);
        if (optionalUser.isEmpty()){
            throw new RuntimeException("User not found with ID: " + userID);
        }

        Optional<Rent> optionalRent = _rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate);
        if (optionalRent.isEmpty()){
            throw new RuntimeException("User: \"" + userID + "\" did not rent the game: \"" + gameID + "\" on date :" + startDate);
        }

        

        Rent rent = optionalRent.get(); 
        LocalDate endDate = rent.getEndDate();

        LocalDate actualDate = LocalDate.now();
        if (actualDate.isAfter(endDate)) {
            throw new RuntimeException("Can't extend a rent that is over");
        }

        User user = optionalUser.get();
        Game game = optionalGame.get();
        double balance = user.getBalance();
        double price = game.getPrice();
        price *= length;
        
        if(balance < price){
            throw new RuntimeException("User dose not have enough money");
        }
        
        user.addToBalance(-price);
        _userRepository.save(user);
        rent.addToEndDate(length);
        rent.addToPrice(price);

        return _rentRepository.save(rent);
    }

    public void deleteGame(String id) {
        _gameRepository.deleteById(id);
    }

    public void deleteAllGames(){
        _gameRepository.deleteAll();
    }

    public void deleteAllrents(){
        _rentRepository.deleteAll();
    }

public String addReview(String userId, String gameId, String reviewText, int rating) {
    
    Optional<Game> gameOpt = _gameRepository.findById(gameId);
    if (gameOpt.isEmpty()) {
        throw new RuntimeException("Jocul nu există!");
    }
    Game game = gameOpt.get();

    
    if (rating < 1 || rating > 5) {
        throw new RuntimeException("Rating-ul trebuie să fie între 1 și 5!");
    }
    if(reviewText.length() == 0){
        throw new RuntimeException("Review-ul nu poate fi gol!");
    }

   
    Optional<User> userOpt = _userRepository.findById(userId);
    if (userOpt.isEmpty()) {
        throw new RuntimeException("Utilizatorul nu există!");
    }
    
    User user = userOpt.get();

   
    List<Rent> rentals = _rentRepository.findAll();
    boolean hasRentedGame = rentals.stream()
            .anyMatch(r -> r.getUserID().equals(userId) && r.getGameID().equals(gameId));

    if (!hasRentedGame) {
        throw new RuntimeException("Nu poți lăsa un review pentru un joc pe care nu l-ai închiriat!");
    }

  

   
    boolean alreadyReviewed = game.getReviews().stream().anyMatch(r -> r.getUserId().equals(userId));
    if (alreadyReviewed) {
        throw new RuntimeException("Ai lăsat deja un review pentru acest joc!");
    }

   
    game.addReview(new Review(userId, reviewText, rating));
    _gameRepository.save(game);

    return "Review adăugat cu succes!";
}

    public List<Review> getReviewsForGame(String gameId) {
        Optional<Game> gameOpt = _gameRepository.findById(gameId);
        if (gameOpt.isEmpty()) {
            throw new RuntimeException("Jocul nu există!");
        }
        return gameOpt.get().getReviews();
    }
}
