package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/Users")
public class UserController {

    private final UserService _userService;

    public UserController(UserService userService) {
        _userService = userService;
    }


    @GetMapping("/GetAll")
    public List<User> getAllUsers() {
        return _userService.getAllUsers();
    }

    @GetMapping("/Find/{id}")
    public Optional<User> GetUserByID(@PathVariable int id){
        return _userService.GetUserByID(id);
    }

    @PostMapping("/Add")
    public User addUser(@RequestBody User user){
        return _userService.addUser(user);
    }

    @PatchMapping("/AddMoney")
    public User addMoney(@RequestParam int userID, @RequestParam double sum){
        try {
            return _userService.addMoney(userID, sum);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
    
    

    @DeleteMapping("/DeleteAll")
    public ResponseEntity<String> deleteAllUsers() {
        _userService.deleteAllUsers();
        return ResponseEntity.ok("All users deleted successfully");
    }


}