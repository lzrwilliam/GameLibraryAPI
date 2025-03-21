package ro.unibuc.hello.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CounterService counterService;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User(1, "John", "Doe", "johndoe", "password123",
                LocalDate.of(2000, 5, 10), 100.0, "john.doe@example.com", "123456789");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser));

        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("John", users.get(0).getfName());
    }

    @Test
    void testGetUserById_UserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.GetUserByID(1);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getfName());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        Optional<User> result = userService.GetUserByID(2);

        assertFalse(result.isPresent());
    }

  @Test
void testAddUser() {
    when(counterService.getNextSequence("users")).thenReturn(1);
    when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

    User newUser = new User(0, "Jane", "Doe", "janedoe", "password456",
            LocalDate.of(1995, 3, 15), 50.0, "jane.doe@example.com", "987654321");

    User result = userService.addUser(newUser);

    assertNotNull(result);
    assertEquals("Jane", result.getfName()); // Acum testul va trece
    verify(userRepository, times(1)).save(any(User.class));
}


    @Test
    void testAddMoney_UserExists() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = userService.addMoney(1, 50.0);

        assertNotNull(updatedUser);
        assertEquals(150.0, updatedUser.getBalance());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void testAddMoney_UserNotFound() {
        when(userRepository.findById(2)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userService.addMoney(2, 50.0);
        });

        assertEquals("User not found with ID: 2", thrown.getMessage());
    }

    @Test
    void testDeleteAllUsers() {
        userService.deleteAllUsers();

        verify(userRepository, times(1)).deleteAll();
    }
}
