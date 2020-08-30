package com.example.demo.controllers;

import com.example.demo.TestUtils;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        itemController = new ItemController();
        TestUtils.injectObject(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> listItem = Arrays.asList(item);

        when(itemRepository.findAll()).thenReturn(listItem);

        final ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        assertEquals(item.getId(), itemList.get(0).getId());

    }

    @Test
    public void get_item_by_id_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        Optional<Item> optionalItem = Optional.of(item);

        when(itemRepository.findById(1L)).thenReturn(optionalItem);

        final ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item newItem = response.getBody();
        assertEquals(item.getId(), newItem.getId());

    }

    @Test
    public void get_item_by_name_happy_path() throws Exception {

        Item item = new Item();
        item.setId(1l);
        item.setName("Round Widget");
        item.setPrice(BigDecimal.valueOf(2.99));
        item.setDescription("A widget that is round");

        List<Item> listItem = Arrays.asList(item);

        when(itemRepository.findByName("Round Widget")).thenReturn(listItem);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Round Widget");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        assertEquals(item.getId(), itemList.get(0).getId());

    }

    @Test
    public void get_item_by_name_not_found() throws Exception {

        final ResponseEntity<List<Item>> response = itemController.getItemsByName("Flat Widget");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }


}
