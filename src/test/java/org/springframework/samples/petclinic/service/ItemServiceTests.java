
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ItemServiceTests {

	@Autowired
	protected ItemService itemService;


	@Test
	void shouldSaveItem() {
		Item i = new Item();
		i.setQuantity(1);
		i.setUnitPrice(10.20);
		i.setId(3);
		i.setOrder(null);
		Product product = new Product();
		product.setName("Producto de prueba");
		product.setUrlImage("http://www.urldeprueba.com");
		product.setDescription("Descripción de prueba");
		product.setCategory(Category.ACCESORY);
		product.setUnitPrice(10.20);
		product.setStock(20);
		product.setId(1);
		i.setProduct(product);
		ShoppingCart shoppingCart = new ShoppingCart();
		Owner owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		owner.setUser(user);
		shoppingCart.setOwner(owner);
		shoppingCart.setId(2);
		i.setShoppingCart(shoppingCart);
		Integer beforeSave = this.itemService.findItemsInShoppingCart(2).size();
		this.itemService.saveItem(i);
		Integer afterSave = this.itemService.findItemsInShoppingCart(2).size();
		Assertions.assertTrue(beforeSave + 1 == afterSave);
	}

	/*
	 * @Test
	 * void shouldNotSaveItem() {
	 * Item i = new Item();
	 * i.setId(3);
	 * i.setOrder(null);
	 * Product product = new Product();
	 * product.setName("Producto de prueba");
	 * product.setUrlImage("http://www.urldeprueba.com");
	 * product.setDescription("Descripción de prueba");
	 * product.setCategory(Category.ACCESORY);
	 * product.setUnitPrice(10.20);
	 * product.setStock(20);
	 * product.setId(1);
	 * i.setProduct(product);
	 * ShoppingCart shoppingCart = new ShoppingCart();
	 * Owner owner = new Owner();
	 * owner.setFirstName("Sam");
	 * owner.setLastName("Schultz");
	 * owner.setAddress("4, Evans Street");
	 * owner.setCity("Wollongong");
	 * owner.setTelephone("4444444444");
	 * User user = new User();
	 * user.setUsername("Sam");
	 * user.setPassword("supersecretpassword");
	 * user.setEnabled(true);
	 * user.setEmail("email@bien.com");
	 * owner.setUser(user);
	 * shoppingCart.setOwner(owner);
	 * shoppingCart.setId(2);
	 * i.setShoppingCart(shoppingCart);
	 * Integer beforeSave = this.itemService.findItemsInShoppingCart(2).size();
	 * this.itemService.saveItem(i);
	 * Integer afterSave = this.itemService.findItemsInShoppingCart(2).size();
	 * Assertions.assertTrue(beforeSave == afterSave);
	 *
	 * }
	 */

	@Test
	void shouldFindItemsInShoppingCart() {
		List<Item> l = this.itemService.findItemsInShoppingCart(1);
		Assertions.assertTrue(!l.isEmpty());
	}

	@Test
	void shouldNotFindItemsInShoppingCart() {
		List<Item> l = this.itemService.findItemsInShoppingCart(2);
		Assertions.assertTrue(l.isEmpty());
	}

	@Test
	void shouldCheckIfItemIsIntheShoppingCart() {
		Item i = this.itemService.checkIfItemIsIntheShoppingCart(1, 1);
		Assertions.assertTrue(!i.equals(null));
	}

	@Test
	void shouldNotCheckIfItemIsIntheShoppingCart() {
		Item i = this.itemService.checkIfItemIsIntheShoppingCart(5, 1);
		Assertions.assertTrue(i == null);
	}

	@Test
	void shouldDeleteItem() {
		List<Item> l = this.itemService.findItemsInShoppingCart(1);
		Integer foundBefore = l.size();
		this.itemService.deleteItem(1);
		l = this.itemService.findItemsInShoppingCart(1);
		Integer foundAfter = l.size();
		Assertions.assertTrue(foundBefore - 1 == foundAfter);
	}

	@Test
	void shouldNotDeleteItem() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.itemService.deleteItem(4));
	}

	@Test
	void shouldFindItemById() {
		Item i = this.itemService.findItemById(1);
		Assertions.assertTrue(!i.equals(null));
	}

	@Test
	void shouldNotFindItemById() {
		Item i = this.itemService.findItemById(7);
		Assertions.assertTrue(i == null);
	}

}
