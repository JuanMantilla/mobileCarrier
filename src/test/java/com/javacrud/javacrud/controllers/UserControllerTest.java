// package com.javacrud.javacrud.controllers;

// import static org.mockito.Mockito.when;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.any;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// import java.util.UUID;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
// import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.javacrud.javacrud.documents.User;
// import com.javacrud.javacrud.services.UserService;
// import com.javacrud.javacrud.util.UserDTO;

// @WebMvcTest(UserController.class)
// public class UserControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Mock
//     private UserService userService;

//     @InjectMocks
//     private UserController userController;

//     @BeforeEach
//     public void setUp() {
//         mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//     }

//     @Test
//     public void testCreateUser() throws Exception {
//         User user = new User(UUID.randomUUID().toString(), "John", "Doe", "john.doe@example.com");
//         UserDTO userDto = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
//         when(userService.create(any(User.class))).thenReturn(user);

//         ObjectMapper mapper = new ObjectMapper();
//         String jsonRequest = mapper.writeValueAsString(user);

//         mockMvc.perform(MockMvcRequestBuilders
//                 .post("/users")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(jsonRequest)
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").exists())
//                 .andExpect(jsonPath("$.firstName").value("John"))
//                 .andExpect(jsonPath("$.lastName").value("Doe"))
//                 .andExpect(jsonPath("$.email").value("john.doe@example.com"));

//         verify(userService, times(1)).create(any(User.class));
//     }

//     @Test
//     public void testGetUser() throws Exception {
//         String userId = UUID.randomUUID().toString();
//         User user = new User(userId, "John", "Doe", "john.doe@example.com");
//         UserDTO userDto = new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
//         when(userService.get(userId)).thenReturn(user);

//         mockMvc.perform(MockMvcRequestBuilders
//                 .get("/users/" + userId)
//                 .accept(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$.id").value(userId))
//                 .andExpect(jsonPath("$.firstName").value("John"))
//                 .andExpect(jsonPath("$.lastName").value("Doe"))
//                 .andExpect(jsonPath("$.email").value("john.doe@example.com"));

//         verify(userService, times(1)).get(userId);
//     }

//     // Similarly, write tests for getUsers() and updateUser() methods
// }
