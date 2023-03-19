package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final BCryptPasswordEncoder bCryptPasswordEncoder = mock(BCryptPasswordEncoder.class);
    private User user;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        user = new User("test", "thisIsHashed");
        user.setId(1);
        when(bCryptPasswordEncoder.encode("TestPassword")).thenReturn("thisIsHashed");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByUsername(anyString())).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(user));
    }

    @Test
    public void create_user_happy_path() throws Exception {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("TestPassword");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        User responseBody = response.getBody();

        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.getId());
        Assert.assertEquals("test", responseBody.getUsername());
        Assert.assertEquals("thisIsHashed", responseBody.getPassword());

        final ResponseEntity<User> response2 = userController.findByUserName(responseBody.getUsername());

        Assert.assertNotNull(response2);
        Assert.assertEquals(200, response2.getStatusCodeValue());
        User responseBody2 = response2.getBody();

        Assert.assertNotNull(responseBody2);
        Assert.assertEquals(1, responseBody2.getId());
        Assert.assertEquals("test", responseBody2.getUsername());
        Assert.assertEquals("thisIsHashed", responseBody2.getPassword());

        final ResponseEntity<User> response3 = userController.findById(responseBody.getId());
        User responseBody3 = response3.getBody();

        Assert.assertNotNull(responseBody3);
        Assert.assertEquals(1, responseBody3.getId());
        Assert.assertEquals("test", responseBody3.getUsername());
        Assert.assertEquals("thisIsHashed", responseBody3.getPassword());
    }

    @Test
    public void create_user_faill() {
        CreateUserRequest userRequest = new CreateUserRequest();
        userRequest.setUsername("test");
        userRequest.setPassword("pass");

        final ResponseEntity<User> response = userController.createUser(userRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(400, response.getStatusCodeValue());
        Assert.assertEquals("BAD_REQUEST", response.getStatusCode().toString().substring(4));
    }
}
