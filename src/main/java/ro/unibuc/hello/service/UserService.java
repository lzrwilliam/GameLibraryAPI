package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{
    private final UserRepository _userRepository;

    public UserService(UserRepository userRepository) {
        _userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return _userRepository.findAll();
    }

    public Optional<User> GetUserByID(String id){
        return _userRepository.findById(id);
    }

    public User addUser(User user){
        return _userRepository.save(user);
        
    }

    public User addMoney(String id, double sum){
        Optional<User> optionalUser = _userRepository.findById(id);
        if(optionalUser.isEmpty()){
            throw new RuntimeException("User not found with ID: " + id);
        }

        User user = optionalUser.get();
        user.addToBalance(sum);
        
        return _userRepository.save(user);
    }

    public void deleteAllUsers() {
        _userRepository.deleteAll();
    }
}