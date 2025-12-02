package com.example.hogwartsartifactsonline.hogwartsuser;

import com.example.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> users;

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

        users = List.of(user1, user2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetByIdSuccess() {
        //Given
        HogwartsUser user1 = new HogwartsUser();
        user1.setId(1);
        user1.setUsername("John");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("admin user");
        given(userRepository.findById(1)).willReturn(Optional.of(user1));
        //When
        HogwartsUser user = userService.getById(1);
        //Then
        assertThat(user.getId()).isEqualTo(user1.getId());
        assertThat(user.getUsername()).isEqualTo(user1.getUsername());
        assertThat(user.getRoles()).isEqualTo(user1.getRoles());
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testGetByIdNotFound() {
        //Given
        Integer userId = 3;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //When
        Throwable throwable = catchThrowable(() -> userService.getById(userId));
        //Then
        assertThat(throwable).isInstanceOf(ObjectNotFoundException.class).hasMessage("Could not find user with id: " + userId + ".");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAllUsersSuccess() {
        //Given
        given(userRepository.findAll()).willReturn(users);
        //When
        List<HogwartsUser> foundUsers = userService.getAllUsers();
        //Then
        assertThat(foundUsers.size()).isEqualTo(users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testAddUserSuccess() {
        //Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("Alice");
        newUser.setPassword("password");
        newUser.setRoles("user");
        newUser.setEnabled(true);
        given(userRepository.save(newUser)).willReturn(newUser);
        //when
        HogwartsUser savedUser = userService.save(newUser);
        //Then
        assertThat(savedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(savedUser.getRoles()).isEqualTo(newUser.getRoles());
        verify(userRepository, times(1)).save(newUser);
    }

    @Test
    void testUpdateUserSuccess() {
        //Given
        HogwartsUser user = new HogwartsUser();
        user.setId(1);
        user.setUsername("Alice");
        user.setPassword("password");
        user.setRoles("user");
        user.setEnabled(true);

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(1);
        updatedUser.setUsername("AliceUpdated");
        updatedUser.setPassword("password");
        updatedUser.setRoles("admin user");
        updatedUser.setEnabled(false);
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        //When
        HogwartsUser result = userService.update(1, updatedUser);
        //Then
        assertThat(result.getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(result.getRoles()).isEqualTo(updatedUser.getRoles());
        assertThat(result.isEnabled()).isEqualTo(updatedUser.isEnabled());
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(updatedUser);

    }

    @Test
    void testUpdateUserNotFound() {
        //Given
        Integer userId = 3;
        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setUsername("testuser");
        updatedUser.setPassword("nopass");
        updatedUser.setRoles("user");
        updatedUser.setEnabled(true);
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            userService.update(userId, updatedUser);
        });
        //Then
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testDeleteSuccess() {
        HogwartsUser user = new HogwartsUser();
        user.setId(1);
        user.setUsername("Alice");
        user.setPassword("password");
        user.setRoles("user");
        user.setEnabled(true);
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);
        //When
        userService.delete(1);
        //Then
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound() {
        //Given
        Integer userId = 3;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        //When
        assertThrows(ObjectNotFoundException.class, () -> {
            userService.delete(userId);
        });
        //Then
        verify(userRepository, times(1)).findById(userId);
    }
}