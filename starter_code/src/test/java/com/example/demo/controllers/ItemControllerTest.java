package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItems_success() {
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        when(itemRepository.findAll()).thenReturn(List.of(item));


        ResponseEntity<List<Item>> itemResponse = itemController.getItems();


        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        List<Item> itemList = itemResponse.getBody();
        assertEquals("test item", itemList.get(0).getName());
        assertEquals(BigDecimal.valueOf(1.99), itemList.get(0).getPrice());
    }

    @Test
    public void getItems_empty() {
        when(itemRepository.findAll()).thenReturn(Collections.emptyList());


        ResponseEntity<List<Item>> itemResponse = itemController.getItems();


        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        List<Item> itemList = itemResponse.getBody();
        assertTrue(itemList.isEmpty());
    }

    @Test
    public void getItemById_success() {
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));


        ResponseEntity<Item> itemResponse = itemController.getItemById(0L);


        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        Item itemBody = itemResponse.getBody();
        assertEquals("test item", itemBody.getName());
        assertEquals(BigDecimal.valueOf(1.99), itemBody.getPrice());
    }

    @Test
    public void getItemById_error() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());


        ResponseEntity<Item> itemResponse = itemController.getItemById(0L);


        assertNotNull(itemResponse);
        assertEquals(404, itemResponse.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_success() {
        Item item = new Item();
        item.setId(0L);
        item.setName("test item");
        item.setDescription("test item description");
        item.setPrice(BigDecimal.valueOf(1.99));
        List<Item> items = List.of(item);
        when(itemRepository.findByName(any())).thenReturn(items);


        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName("test item");


        assertNotNull(itemResponse);
        assertEquals(200, itemResponse.getStatusCodeValue());
        List<Item> itemList = itemResponse.getBody();
        assertEquals("test item", itemList.get(0).getName());
        assertEquals(BigDecimal.valueOf(1.99), itemList.get(0).getPrice());
    }

    @Test
    public void getItemsByName_error() {
        when(itemRepository.findByName(any())).thenReturn(null);


        ResponseEntity<List<Item>> itemResponse = itemController.getItemsByName("test item");


        assertNotNull(itemResponse);
        assertEquals(404, itemResponse.getStatusCodeValue());
    }
}