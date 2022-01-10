package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_success() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        Cart cart = new Cart();
        cart.setId(0L);
        cart.setUser(newUser);
        cart.setItems(List.of(item));
        cart.setTotal(BigDecimal.valueOf(1.99));
        newUser.setCart(cart);
        UserOrder userOrder = new UserOrder();
        userOrder.setId(0L);
        userOrder.setUser(newUser);
        userOrder.setItems(List.of(item));
        userOrder.setTotal(BigDecimal.valueOf(1.99));
        when(userRepository.findByUsername(any())).thenReturn(newUser);
        when(orderRepository.save(any())).thenReturn(userOrder);


        ResponseEntity<UserOrder> orderResponse = orderController.submit("Tester");


        assertNotNull(orderResponse);
        assertEquals(200, orderResponse.getStatusCodeValue());
        UserOrder orderDetails = orderResponse.getBody();
        assertEquals("Tester", orderDetails.getUser().getUsername());
        assertEquals(BigDecimal.valueOf(1.99), orderDetails.getTotal());
        assertEquals("test item", orderDetails.getItems().get(0).getName());
    }

    @Test
    public void submit_error() {
        when(userRepository.findByUsername(any())).thenReturn(null);


        ResponseEntity<UserOrder> orderResponse = orderController.submit("Tester");


        assertNotNull(orderResponse);
        assertEquals(404, orderResponse.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser_success() {
        User newUser = new User();
        newUser.setUsername("Tester");
        newUser.setPassword("test123456");
        newUser.setId(0);
        newUser.setCart(new Cart());
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        List<Item> orderItems = List.of(item);
        UserOrder userOrder = new UserOrder();
        userOrder.setId(0L);
        userOrder.setUser(newUser);
        userOrder.setItems(orderItems);
        userOrder.setTotal(BigDecimal.valueOf(1.99));
        List<UserOrder> userOrderHistory = List.of(userOrder);
        when(userRepository.findByUsername(any())).thenReturn(newUser);
        when(orderRepository.findByUser(any())).thenReturn(userOrderHistory);


        ResponseEntity<List<UserOrder>> orderResponse = orderController.getOrdersForUser("Tester");


        assertNotNull(orderResponse);
        assertEquals(200, orderResponse.getStatusCodeValue());
        List<UserOrder> orderHistory = orderResponse.getBody();
        assertEquals("Tester", orderHistory.get(0).getUser().getUsername());
        assertEquals(BigDecimal.valueOf(1.99), orderHistory.get(0).getTotal());
    }

    @Test
    public void getOrdersForUser_error() {
        when(userRepository.findByUsername(any())).thenReturn(null);


        ResponseEntity<List<UserOrder>> orderResponse = orderController.getOrdersForUser("Tester");


        assertNotNull(orderResponse);
        assertEquals(404, orderResponse.getStatusCodeValue());
    }

}