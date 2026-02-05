package com.omnistore.service;

import com.omnistore.entity.User;
import com.omnistore.exception.BadRequestException;
import com.omnistore.exception.ResourceNotFoundException;
import com.omnistore.repository.UserRepository;
import com.omnistore.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User registeredUser = userService.registerUser(user.getEmail(), user.getPassword());

        assertNotNull(registeredUser);
        assertEquals(user.getEmail(), registeredUser.getEmail());
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> {
            userService.registerUser(user.getEmail(), user.getPassword());
        });
    }

    @Test
    void testFindUserByEmail_Success() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User foundUser = userService.findUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
    }

    @Test
    void testFindUserByEmail_NotFound() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.findUserByEmail(user.getEmail());
        });
    }
}
