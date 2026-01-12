package com.gl.authentication.service.impl;

import com.gl.authentication.dto.CreateUserRequest;
import com.gl.authentication.dto.PhoneRequest;
import com.gl.authentication.dto.UserResponse;
import com.gl.authentication.entity.User;
import com.gl.authentication.exception.UserAlreadyExistsException;
import com.gl.authentication.repository.UserRepository;
import com.gl.authentication.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private UserServiceImpl userService;

    private CreateUserRequest validRequest;

    @BeforeEach
    void setup() {
        validRequest = new CreateUserRequest();
        validRequest.setEmail("test@mail.com");
        validRequest.setPassword("a2asfGf4");
        validRequest.setName("Test User");

        PhoneRequest phone = new PhoneRequest();
        phone.setNumber("123456");
        phone.setCityCode("");
        phone.setCountryCode("57");

        validRequest.setPhones(Collections.singletonList(phone));
    }

    @Test
    void createUser_success() {
        when(userRepository.existsByEmail("test@mail.com")).thenReturn(false);
        when(jwtProvider.generateToken(anyString())).thenReturn("jwt-token");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse response = userService.createUser(validRequest);

        assertNotNull(response);
        assertEquals("test@mail.com", response.getEmail());
        assertEquals("jwt-token", response.getToken());
        assertTrue(response.isActive());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_userAlreadyExists() {
        when(userRepository.existsByEmail("test@mail.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> userService.createUser(validRequest));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_success() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        UserResponse response = userService.getById("1");

        assertNotNull(response);
        assertEquals("test@mail.com", response.getEmail());
    }

    @Test
    void getById_notFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> userService.getById("1"));
    }

    @Test
    void getAll_success() {
        User user = new User();
        user.setEmail("test@mail.com");

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponse> responses = userService.getAll();

        assertEquals(1, responses.size());
        assertEquals("test@mail.com", responses.get(0).getEmail());
    }

    @Test
    void login_success() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);

        when(jwtProvider.getSubject("old-token")).thenReturn("test@mail.com");
        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(jwtProvider.generateToken("test@mail.com")).thenReturn("new-token");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse response = userService.login("old-token");

        assertNotNull(response);
        assertEquals("test@mail.com", response.getEmail());
        assertEquals("new-token", response.getToken());
    }
}
