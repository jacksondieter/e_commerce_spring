package com.example.demo.controller;

import com.example.demo.TestUtils;
import com.example.demo.controllers.CartController;
import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {
    private OrderController orderController;
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private UserOrder order;
    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        Cart cart = new Cart();
        cart.setId(1L);
        Item item = new Item("Round Widget", new BigDecimal("2.99"), "A widget that is round");
        item.setId(1L);
        User user = new User("test", "thisIsHashed");
        user.setId(1);
        user.setCart(cart);
        cart.setUser(user);
        cart.addItem(item);
        cart.addItem(item);
        UserOrder order = UserOrder.createFromCart(cart);
        order.setId(1L);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));
        when(userRepository.findByUsername("test")).thenReturn(user);
    }

    @Test
    public void submit_order(){
        final ResponseEntity<UserOrder> response = orderController.submit("test");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        UserOrder responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.getId().longValue());
        Assert.assertEquals("test", responseBody.getUser().getUsername());
        Assert.assertEquals(new BigDecimal("5.98"), responseBody.getTotal());
    }

    @Test
    public void get_orders_for_user(){
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        Assert.assertNotNull(response);
        Assert.assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> responseBody = response.getBody();
        Assert.assertNotNull(responseBody);
        Assert.assertEquals(1, responseBody.size());
        Assert.assertEquals(1L, responseBody.get(0).getItems().get(0).getId().longValue());
        Assert.assertEquals(new BigDecimal("5.98"), responseBody.get(0).getTotal());
    }

    @Test
    public void submit_order_user_not_found(){
        final ResponseEntity<UserOrder> response = orderController.submit("user_not_found");

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        UserOrder responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }

    @Test
    public void get_orders_for_user_user_not_found(){
        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("user_not_found");

        Assert.assertNotNull(response);
        Assert.assertEquals(404, response.getStatusCodeValue());

        List<UserOrder> responseBody = response.getBody();
        Assert.assertEquals(null,responseBody);
    }
}
