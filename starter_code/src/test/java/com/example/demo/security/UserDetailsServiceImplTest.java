package com.example.demo.security;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;

    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        userDetailsService = new UserDetailsServiceImpl();
        TestUtils.injectObjects(userDetailsService, "userRepository", userRepository);
    }

    @Test
    public void loadUserByUsername_success() {
        User user = new User();
        user.setId(0);
        user.setUsername("Tester");
        user.setPassword("test123456");
        when(userRepository.findByUsername(any())).thenReturn(user);


        UserDetails userDetails = userDetailsService.loadUserByUsername("Tester");


        assertNotNull(userDetails);
        assertEquals("Tester", userDetails.getUsername());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_error() {
        when(userRepository.findByUsername(any())).thenReturn(null);


        userDetailsService.loadUserByUsername("Tester");
    }

}