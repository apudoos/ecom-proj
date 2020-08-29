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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.Assert.*;
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
        TestUtils.injectObject(cartController, "userRepository", userRepository);
        TestUtils.injectObject(cartController, "cartRepository", cartRepository);
        TestUtils.injectObject(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_cart_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");


        Cart cart = new Cart();
        cart.setId(1L);
        //cart.setItems(Arrays.asList(item));


        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        Optional<Item> oItem = Optional.of(item);
        //Optional<User> oUser = Optional.of(user);

        when(itemRepository.findById(1l)).thenReturn(oItem);
        when(userRepository.findByUsername("test")).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart newCart = response.getBody();
        assertNotNull(newCart);

        Long id = newCart.getId();
        assertEquals(Long.valueOf(1), id);

    }

    @Test
    public void remove_cart_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");


        Cart cart = new Cart();
        cart.setId(1L);
        //cart.setItems(Arrays.asList(item));


        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("password");
        user.setCart(cart);

        Optional<Item> oItem = Optional.of(item);
        //Optional<User> oUser = Optional.of(user);

        when(itemRepository.findById(1l)).thenReturn(oItem);
        when(userRepository.findByUsername("test")).thenReturn(user);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart newCart = response.getBody();
        assertNotNull(newCart);
        assertTrue(newCart.getItems().isEmpty());

    }

    @Test
    public void is_user_void_in_add_cart() {

        when(userRepository.findByUsername(" ")).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(" ");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void is_user_void_in_remove_cart() {

        when(userRepository.findByUsername(" ")).thenReturn(new User());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(" ");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void is_item_void_in_add_cart() {
        User user = new User();
        user.setId(1L);

        Optional<Item> oo = Optional.empty();

        when(userRepository.findByUsername(" ")).thenReturn(user);
        when(itemRepository.findById(1l)).thenReturn(oo);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(" ");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void is_item_void_in_remove_cart() {
        User user = new User();
        user.setId(1L);

        Optional<Item> oo = Optional.empty();

        when(userRepository.findByUsername(" ")).thenReturn(user);
        when(itemRepository.findById(1l)).thenReturn(oo);

        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(" ");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertEquals(404, response.getStatusCodeValue());

    }


}
