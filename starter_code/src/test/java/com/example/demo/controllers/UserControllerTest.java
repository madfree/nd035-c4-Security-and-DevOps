package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private BCryptPasswordEncoder passwordEncoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", passwordEncoder);
    }

    @Test
    public void testFindById_success() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        when(userRepository.findById(any())).thenReturn(java.util.Optional.of(newUser));


        ResponseEntity<User> userResponse = userController.findById(0L);


        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User user = userResponse.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Tester", user.getUsername());
    }

    @Test
    public void testFindById_error() {
        when(userRepository.findByUsername(any())).thenReturn(null);


        ResponseEntity<User> userResponse = userController.findById(0L);


        assertNotNull(userResponse);
        assertEquals(404, userResponse.getStatusCodeValue());
    }

    @Test
    public void testFindByUserName_success() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        when(userRepository.findByUsername(any())).thenReturn(newUser);


        ResponseEntity<User> userResponse = userController.findByUserName("Tester");


        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User user = userResponse.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Tester", user.getUsername());
    }

    @Test
    public void testFindByUserName_error() {
        when(userRepository.findByUsername(any())).thenReturn(null);


        ResponseEntity<User> userResponse = userController.findByUserName("User1");


        assertNotNull(userResponse);
        assertEquals(404, userResponse.getStatusCodeValue());
    }

    @Test
    public void testCreateUser_success() {
        when(passwordEncoder.encode("test123456")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Tester");
        createUserRequest.setPassword("test123456");
        createUserRequest.setConfirmPassword("test123456");


        ResponseEntity<User> userResponse = userController.createUser(createUserRequest);


        assertNotNull(userResponse);
        assertEquals(200, userResponse.getStatusCodeValue());
        User user = userResponse.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals("Tester", user.getUsername());
        assertEquals("thisIsHashed", user.getPassword());
    }

    @Test
    public void testCreateUser_password_error() {
        when(passwordEncoder.encode("test123456")).thenReturn("thisIsHashed");
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Tester");
        createUserRequest.setPassword("test123");
        createUserRequest.setConfirmPassword("test123");


        ResponseEntity<User> userResponse = userController.createUser(createUserRequest);


        assertNotNull(userResponse);
        assertEquals(400, userResponse.getStatusCodeValue());
    }

}