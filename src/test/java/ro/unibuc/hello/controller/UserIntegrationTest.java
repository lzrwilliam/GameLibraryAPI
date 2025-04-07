package ro.unibuc.hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ro.unibuc.hello.data.model.User;
import ro.unibuc.hello.data.repository.UserRepository;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.20")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void mongoProps(DynamicPropertyRegistry registry) {
        String uri = mongoDBContainer.getReplicaSetUrl("testdb");
        registry.add("spring.data.mongodb.uri", () -> uri);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static final User testUser = new User(
            999, "John", "Doe", "johndoe", "password123",
            LocalDate.of(2000, 1, 1), 100.0, "john@doe.com", "1234567890"
    );

    @BeforeEach
    void setup() {
        // Curăță baza de date înainte de fiecare test
        userRepository.deleteAll();
        // Adaugă user-ul de test după curățare
        userRepository.save(testUser);
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/Users/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userName").value("johndoe"));
    }

    @Test
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/Users/Find/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fName").value("John"))
                .andExpect(jsonPath("$.email").value("john@doe.com"));
    }

    @Test
    void testAddMoney() throws Exception {
        mockMvc.perform(patch("/Users/AddMoney")
                .param("userID", "999")
                .param("sum", "50.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(150.0));
    }

    @Test
    void testDeleteAllUsers() throws Exception {
        mockMvc.perform(delete("/Users/DeleteAll"))
                .andExpect(status().isOk())
                .andExpect(content().string("All users deleted successfully"));

        mockMvc.perform(get("/Users/GetAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testAddMoneyToNonExistingUser() throws Exception {
        mockMvc.perform(patch("/Users/AddMoney")
                .param("userID", "1234")
                .param("sum", "50.0"))
                .andExpect(status().isNotFound());
    }
}