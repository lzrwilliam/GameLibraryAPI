package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.Rent;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.model.Game;

import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.repository.RentRepository;
import ro.unibuc.hello.data.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
public class RentService {  
    
    @Autowired
    private RentRepository rentRepository;

      @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    public Rent getRent(int userID, int gameID, LocalDate startDate) {
        return rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate)
                .orElseThrow(() -> new RuntimeException("Rent not found"));
    }

    public Rent rentGame(int gameID, int userID, int length) {
        Optional<Game> optionalGame = gameRepository.findById(gameID);
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalGame.isEmpty()) {
            throw new RuntimeException("Game not found with ID: " + gameID);
        }
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with ID: " + userID);
        }


        Game game = optionalGame.get();
        User user = optionalUser.get();

        List<Rent> userRents = rentRepository.findAll();
        boolean alreadyActive = userRents.stream().anyMatch(r ->
            r.getUserID() == userID &&
            r.getGameID() == gameID &&
            LocalDate.now().isBefore(r.getEndDate())
        );
        if (alreadyActive) {
            throw new RuntimeException("User already has an active rent for this game.");
        }

        if (game.getAvailableCopies() < 1) {
            throw new RuntimeException("No available copies for game: " + gameID);
        }

        double totalPrice = game.getPrice() * length;
        if (user.getBalance() < totalPrice) {
            throw new RuntimeException("User does not have enough money");
        }

        // Update game and user data
        game.setAvailableCopies(game.getAvailableCopies() - 1);
        gameRepository.save(game);

        user.addToBalance(-totalPrice);
        userRepository.save(user);

        // Save rent info
        Rent rent = new Rent(userID, gameID, LocalDate.now(), length, totalPrice);
        return rentRepository.save(rent);
    }

    public Rent extendRent(int gameID, int userID, LocalDate startDate, int length) {
        Optional<Rent> optionalRent = rentRepository.findByUserIDAndGameIDAndStartDate(userID, gameID, startDate);
        if (optionalRent.isEmpty()) {
            throw new RuntimeException("No active rent found for this game and user.");
        }

        Rent rent = optionalRent.get();
        LocalDate endDate = rent.getEndDate();
        if (endDate == null) {
            throw new RuntimeException("Invalid rent record: missing end date.");
        }

        if (LocalDate.now().isAfter(endDate)) {
            throw new RuntimeException("Cannot extend a rent that is over.");
        }

        Optional<Game> optionalGame = gameRepository.findById(gameID);
        Optional<User> optionalUser = userRepository.findById(userID);

        if (optionalGame.isEmpty() || optionalUser.isEmpty()) {
            throw new RuntimeException("Game or User not found.");
        }

        Game game = optionalGame.get();
        User user = optionalUser.get();

        double extraPrice = game.getPrice() * length;
        if (user.getBalance() < extraPrice) {
            throw new RuntimeException("User does not have enough money.");
        }

        user.addToBalance(-extraPrice);
        userRepository.save(user);

        rent.addToEndDate(length);
        rent.addToPrice(extraPrice);
        return rentRepository.save(rent);
    }

    public void deleteAllRents() {
        rentRepository.deleteAll();
    }

    public List<Rent> getAllRents() {
        return rentRepository.findAll();
    }



}
