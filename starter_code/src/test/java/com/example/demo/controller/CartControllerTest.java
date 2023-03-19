package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {
    private CartController cartController;
    private final CartRepository cartRepository = mock(CartRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private Cart cart;
    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        cart = new Cart();
        cart.setId(1L);
        Item item = new Item("Round Widget", new BigDecimal("2.99"), "A widget that is round");
        item.setId(1L);
        User user = new User("test", "thisIsHashed");
        user.setId(1);
        user.setCart(cart);
        cart.setUser(user);
        cart.addItem(item);
        cart.addItem(item);
        when(cartRepository.save(cart)).thenReturn(cart);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void add_to_cart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(8);
        modifyCartRequest.setItemId(1L);
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.getId().longValue());
        Assert.assertEquals("test", responseBody.getUser().getUsername());
        Assert.assertEquals(new BigDecimal("29.90"), responseBody.getTotal());
    }

    @Test
    public void add_to_cart_user_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("user_not_found");
        modifyCartRequest.setQuantity(8);
        modifyCartRequest.setItemId(1L);
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }

    @Test
    public void add_to_cart_item_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(8);
        modifyCartRequest.setItemId(2L);
        final ResponseEntity<Cart> response = cartController.addToCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }

    @Test
    public void remove_to_cart(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.getId().longValue());
        Assert.assertEquals("test", responseBody.getUser().getUsername());
        Assert.assertEquals(new BigDecimal("2.99"), responseBody.getTotal());

        final ResponseEntity<Cart> response2 = cartController.removeFromCart(modifyCartRequest);

        Assert.assertNotNull(response2);
        Assert.assertEquals(200, response.getStatusCodeValue());

        Cart responseBody2 = response.getBody();
        Assert.assertNotNull(responseBody2);
        Assert.assertEquals(1, responseBody2.getId().longValue());
        Assert.assertEquals("test", responseBody2.getUser().getUsername());
        Assert.assertEquals(new BigDecimal("0.00"), responseBody2.getTotal());
    }

    @Test
    public void remove_to_cart_user_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("user_not_found");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1L);
        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }

    @Test
    public void remove_to_cart_item_not_found(){
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("test");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(2L);
        final ResponseEntity<Cart> response = cartController.removeFromCart(modifyCartRequest);

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        Cart responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }
}
