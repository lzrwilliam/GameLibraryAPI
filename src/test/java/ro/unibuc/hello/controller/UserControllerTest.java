package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.service.UserService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {




    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
       


        testUser = new User(1, "John", "Doe", "johndoe", "password123",
                LocalDate.of(2000, 5, 10), 100.0, "john.doe@example.com", "123456789");
    }

    @Test
    void testGetAllUsers() throws Exception {
        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/Users/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fName").value("John"));
    }

    @Test
    void testGetUserById_UserExists() throws Exception {
        when(userService.GetUserByID(1)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/Users/Find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fName").value("John"));
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        when(userService.GetUserByID(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/Find/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddUser() throws Exception {
        when(userService.addUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/Users/Add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fName").value("John"));
    }

    @Test
    void testAddMoney_UserExists() throws Exception {
        when(userService.addMoney(1, 50.0)).thenReturn(new User(1, "John", "Doe", "johndoe", "password123",
                LocalDate.of(2000, 5, 10), 150.0, "john.doe@example.com", "123456789"));
    
        mockMvc.perform(patch("/Users/AddMoney")
                .param("userID", "1")  
                .param("sum", "50.0")) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.0));
    }
    
    @Test
    void testAddMoney_UserNotFound() throws Exception {
        when(userService.addMoney(2, 50.0)).thenThrow(new RuntimeException("User not found with ID: 2"));

        mockMvc.perform(patch("/users/AddMoney/2/50.0"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/Users/DeleteAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("All users deleted successfully"));
    }
}