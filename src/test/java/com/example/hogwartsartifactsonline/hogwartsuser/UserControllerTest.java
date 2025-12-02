package com.example.hogwartsartifactsonline.hogwartsuser;

import com.example.hogwartsartifactsonline.hogwartsuser.dto.UserDto;
import com.example.hogwartsartifactsonline.system.StatusCode;
import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "api.endpoint.base-url=/api/v1"
})
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @Value("${api.endpoint.base-url}")
    String base_url;

    List<HogwartsUser> testUsers;

    @BeforeEach
    void setUp() {
        HogwartsUser user1 = new HogwartsUser();
        user1.setId(1);
        user1.setUsername("John");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("admin user");

        HogwartsUser user2 = new HogwartsUser();
        user2.setId(2);
        user2.setUsername("Jane");
        user2.setPassword("abcdef");
        user2.setRoles("user");
        user2.setEnabled(true);

        testUsers = List.of(user1, user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetUserByIdSuccess() throws Exception {
        //Given
        given(userService.getById(1)).willReturn(testUsers.get(0));
        //When and Then
        this.mockMvc.perform(get(this.base_url +"/users/1").accept("application/json"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(testUsers.get(0).getId()))
                .andExpect(jsonPath("$.data.username").value(testUsers.get(0).getUsername()))
                .andExpect(jsonPath("$.data.roles").value(testUsers.get(0).getRoles()));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        //Given
        given(userService.getById(2)).willThrow(new ObjectNotFoundException("user", "2"));
        //When and Then
        this.mockMvc.perform(get(this.base_url +"/users/2").accept("application/json"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(404))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 2."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testGetAllUsersSuccess() throws Exception {
        //Given
        given(userService.getAllUsers()).willReturn(testUsers);
        //When and Then
        this.mockMvc.perform(get(this.base_url +"/users").accept("application/json"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data[0].id").value(testUsers.get(0).getId()))
                .andExpect(jsonPath("$.data[0].username").value(testUsers.get(0).getUsername()))
                .andExpect(jsonPath("$.data[0].roles").value(testUsers.get(0).getRoles()))
                .andExpect(jsonPath("$.data[1].id").value(testUsers.get(1).getId()))
                .andExpect(jsonPath("$.data[1].username").value(testUsers.get(1).getUsername()))
                .andExpect(jsonPath("$.data[1].roles").value(testUsers.get(1).getRoles()));
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        //Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("Alice");
        newUser.setPassword("pass123");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        HogwartsUser savedUser = new HogwartsUser();
        savedUser.setId(3);
        savedUser.setUsername("Alice");
        savedUser.setPassword("pass123");
        savedUser.setEnabled(true);
        savedUser.setRoles("user");

        given(userService.save(eq(newUser))).willReturn(savedUser);
        //When and Then
        this.mockMvc.perform(post(this.base_url +"/users")
                        .contentType("application/json")
                        .content("""
                                {
                                    "username": "Alice",
                                    "password": "pass123",
                                    "enabled": true,
                                    "roles": "user"
                                }
                                """))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").value(savedUser.getId()))
                .andExpect(jsonPath("$.data.username").value(savedUser.getUsername()))
                .andExpect(jsonPath("$.data.roles").value(savedUser.getRoles()));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        //Given
        UserDto userDto = new UserDto(
                1,
                "JohnUpdated",
                true,
                "admin user"
        );
        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(1);
        updatedUser.setUsername("JohnUpdated");
        updatedUser.setEnabled(true);
        updatedUser.setRoles("admin user");
        given(userService.update(eq(1), Mockito.any(HogwartsUser.class))).willReturn(updatedUser);
        //When and Then
        this.mockMvc.perform(put(this.base_url +"/users/1").contentType("application/json")
                        .content("""
                                {
                                    "id": 1,
                                    "username": "JohnUpdated",
                                    "enabled": true,
                                    "roles": "admin user"
                                }
                                """))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updatedUser.getId()))
                .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        //Given
        Mockito.doNothing().when(userService).delete(1);
        //When and Then
        this.mockMvc.perform(delete(this.base_url +"/users/1"))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        //Given
        Mockito.doThrow(new ObjectNotFoundException("user", "2")).when(userService).delete(2);
        //When and Then
        this.mockMvc.perform(delete(this.base_url +"/users/2"))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find user with id: 2."))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}