package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void addToCart_success() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        newUser.setCart(new Cart());
        when(userRepository.findByUsername(any())).thenReturn(newUser);
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("Tester");
        cartRequest.setItemId(0);
        cartRequest.setQuantity(1);
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));


        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);


        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
        Cart cart = cartResponse.getBody();
        assertNotNull(cart);
        assertEquals("test item", cart.getItems().get(0).getName());
        assertEquals(BigDecimal.valueOf(1.99), cart.getItems().get(0).getPrice());
    }

    @Test
    public void addToCart_error() {
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("Tester");
        cartRequest.setItemId(0);
        cartRequest.setQuantity(1);
        when(userRepository.findByUsername(any())).thenReturn(null);


        ResponseEntity<Cart> cartResponse = cartController.addTocart(cartRequest);


        assertNotNull(cartResponse);
        assertEquals(404, cartResponse.getStatusCodeValue());
    }

    @Test
    public void removeFromCart() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        newUser.setCart(new Cart());
        when(userRepository.findByUsername(any())).thenReturn(newUser);
        ModifyCartRequest cartRequest = new ModifyCartRequest();
        cartRequest.setUsername("Tester");
        cartRequest.setItemId(0);
        cartRequest.setQuantity(1);
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));


        ResponseEntity<Cart> cartResponse = cartController.removeFromcart(cartRequest);


        assertNotNull(cartResponse);
        assertEquals(200, cartResponse.getStatusCodeValue());
    }

}