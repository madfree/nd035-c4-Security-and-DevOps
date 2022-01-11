package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

	public static final Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("ORDER_FAILED - user not found: " + username);
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		orderRepository.save(order);
		ResponseEntity<UserOrder> orderResponse = ResponseEntity.ok(order);
		if (orderResponse.getStatusCode().isError()) {
			log.error("ORDER_FAILED with message: " + orderResponse.getStatusCode().getReasonPhrase());
		} else {
			log.info("ORDER_SUCCESS with id: " + orderResponse.getBody().getId());
		}
		return orderResponse;
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.debug("USER_NOT_FOUND:" + username);
			return ResponseEntity.notFound().build();
		}
		ResponseEntity<List<UserOrder>> userOrderHistory = ResponseEntity.ok(orderRepository.findByUser(user));
		if (userOrderHistory.getStatusCode().isError()) {
			log.warn("ORDER_HISTORY_FAILED - Could not load user order history");
		} else {
			log.debug("ORDER_HISTORY_SUCCESS - Successfully fetched user order history");
		}
		return userOrderHistory;
	}
}
