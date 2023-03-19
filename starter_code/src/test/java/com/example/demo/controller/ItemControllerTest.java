package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.ItemController;
import com.example.demo.controllers.UserController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private Item item;
    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        item = new Item("Round Widget", new BigDecimal("2.99"), "A widget that is round");
        item.setId(1L);
        when(itemRepository.findAll()).thenReturn(Arrays.asList(item));
        when(itemRepository.findById(1L)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.findByName("test")).thenReturn(Arrays.asList(item));
    }

    @Test
    public void item_get_all(){
        final ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.size());
    }
    @Test
    public void item_get_by_id(){
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        Item responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1L, responseBody.getId().longValue());
    }
    @Test
    public void item_get_by_name(){
        final ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());
        List<Item> responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.size());
    }
}
