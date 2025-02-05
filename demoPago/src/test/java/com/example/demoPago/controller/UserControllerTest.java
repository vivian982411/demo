package com.example.demoPago.controller;

import com.example.demoPago.model.User;
import com.example.demoPago.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testGetAllUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setName("John Doe");
        user1.setEmail("john@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Jane Doe");
        user2.setEmail("jane@example.com");

        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // Act
        List<User> users = userController.getAllUsers();

        // Assert
        assertEquals(2, users.size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_Found() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<User> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Doe", response.getBody().getName());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserById_NotFound() {
        // Arrange
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.getUserById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testCreateUser() {
        // Arrange
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john@example.com");

        when(userService.saveUser(user)).thenReturn(user);

        // Act
        ResponseEntity<User> createdUser = userController.createUser(user);

        // Assert
        assertNotNull(createdUser);
        assertEquals("John Doe", createdUser.getBody().getName());
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testUpdateUser_Found() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setName("John Doe");
        existingUser.setEmail("john@example.com");

        User updatedUser = new User();
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(existingUser));
        when(userService.saveUser(existingUser)).thenReturn(existingUser);

        // Act
        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("John Updated", response.getBody().getName());
        verify(userService, times(1)).getUserById(1L);
        verify(userService, times(1)).saveUser(existingUser);
    }

    @Test
    public void testUpdateUser_NotFound() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setName("John Updated");
        updatedUser.setEmail("john.updated@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<User> response = userController.updateUser(1L, updatedUser);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userService, times(1)).getUserById(1L);
        verify(userService, never()).saveUser(any());
    }

    @Test
    public void testDeleteUser() {
        // Arrange
        doNothing().when(userService).deleteUser(1L);

        // Act
        ResponseEntity<Void> response = userController.deleteUser(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(1L);
    }
}