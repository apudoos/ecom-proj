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
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepository = mock(UserRepository.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObject(orderController, "userRepository", userRepository);
        TestUtils.injectObject(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void add_order_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(100.00));


        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        cart.setUser(user);


        when(userRepository.findByUsername("test")).thenReturn(user);
        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertEquals(Long.valueOf(1), userOrder.getItems().get(0).getId());
        assertEquals(BigDecimal.valueOf(100.00), userOrder.getTotal());
        assertEquals("test", userOrder.getUser().getUsername());

    }

    @Test
    public void get_order_by_username_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setItems(Arrays.asList(item));
        cart.setTotal(BigDecimal.valueOf(100.00));


        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        cart.setUser(user);

        UserOrder userOrder = new UserOrder();
        userOrder.setId(1l);
        userOrder.setItems(Arrays.asList(item));
        userOrder.setTotal(BigDecimal.valueOf(100.00));
        userOrder.setUser(user);

        List<UserOrder> userOrderList = Arrays.asList(userOrder);

        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(userOrderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder1 = response.getBody().get(0);
        assertEquals(Long.valueOf(1), userOrder.getId());

    }

    @Test
    public void get_order_by_username_error() throws Exception {

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

}
