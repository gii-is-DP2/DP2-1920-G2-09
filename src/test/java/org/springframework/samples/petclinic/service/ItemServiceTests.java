
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
	 * @Test void shouldNotSaveItem() { Item i = new Item(); i.setId(3);
	 * i.setOrder(null); Product product = new Product();
	 * product.setName("Producto de prueba");
	 * product.setUrlImage("http://www.urldeprueba.com");
	 * product.setDescription("Descripción de prueba");
	 * product.setCategory(Category.ACCESORY); product.setUnitPrice(10.20);
	 * product.setStock(20); product.setId(1); i.setProduct(product); ShoppingCart
	 * shoppingCart = new ShoppingCart(); Owner owner = new Owner();
	 * owner.setFirstName("Sam"); owner.setLastName("Schultz");
	 * owner.setAddress("4, Evans Street"); owner.setCity("Wollongong");
	 * owner.setTelephone("4444444444"); User user = new User();
	 * user.setUsername("Sam"); user.setPassword("supersecretpassword");
	 * user.setEnabled(true); user.setEmail("email@bien.com"); owner.setUser(user);
	 * shoppingCart.setOwner(owner); shoppingCart.setId(2);
	 * i.setShoppingCart(shoppingCart); Integer beforeSave =
	 * this.itemService.findItemsInShoppingCart(2).size();
	 * this.itemService.saveItem(i); Integer afterSave =
	 * this.itemService.findItemsInShoppingCart(2).size();
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
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.itemService.deleteItem(999999));
	}

	@Test
	void shouldFindItemById() {
		Item i = this.itemService.findItemById(1);
		Assertions.assertTrue(!i.equals(null));
	}

	@Test
	void shouldNotFindItemById() {
		Item i = this.itemService.findItemById(9999);
		Assertions.assertTrue(i == null);
	}

	// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "1,5,10.20,nombreProduct1,http://www.urldeprueba.com,descripción1, FOOD, 20,1",
			"2,10,100.20,nombreProduct2,http://www.urldeprueba.com,descripción2, FOOD, 10,2",
			"3,1,43.10,nombreProduct3,http://www.urldeprueba.com,descripción3, FOOD, 2000,3" })
	void shouldSaveItemParametrized(final Integer itemId, final Integer quantity, final Double unitPrice,
			final String productName, final String urlProduct, final String productDescription,
			final Category productCategory, final Integer productStock, final Integer productId) {
		Item i = new Item();
		i.setQuantity(quantity);
		i.setUnitPrice(unitPrice);
		i.setId(itemId);
		i.setOrder(null);
		Product product = new Product();
		product.setName(productName);
		product.setUrlImage(urlProduct);
		product.setDescription(productDescription);
		product.setCategory(productCategory);
		product.setUnitPrice(unitPrice);
		product.setStock(productStock);
		product.setId(productId);
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

	@ParameterizedTest
	@CsvSource({ "1", "2" })
	void shouldCheckIfItemIsIntheShoppingCartParametrized(final Integer itemId) {
		Item i = this.itemService.checkIfItemIsIntheShoppingCart(1, itemId);
		Assertions.assertTrue(!i.equals(null));
	}

	@ParameterizedTest
	@CsvSource({ "111", "222", "333" })
	void shouldNotCheckIfItemIsIntheShoppingCartParametrized(final Integer itemId) {
		Item i = this.itemService.checkIfItemIsIntheShoppingCart(1, itemId);
		Assertions.assertTrue(i == null);
	}

	@ParameterizedTest
	@CsvSource({ "1", "2" })
	void shouldDeleteItemParametrized(final Integer itemId) {
		List<Item> l = this.itemService.findItemsInShoppingCart(1);
		Integer foundBefore = l.size();
		this.itemService.deleteItem(itemId);
		l = this.itemService.findItemsInShoppingCart(1);
		Integer foundAfter = l.size();
		Assertions.assertTrue(foundBefore - 1 == foundAfter);
	}

	@ParameterizedTest
	@CsvSource({ "111", "222", "333" })
	void shouldNotDeleteItemParametrize(final Integer itemId) {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.itemService.deleteItem(itemId));
	}

	@ParameterizedTest
	@CsvSource({ "1", "2", "3" })
	void shouldFindItemByIdParametrized(final Integer itemId) {
		Item i = this.itemService.findItemById(itemId);
		Assertions.assertTrue(!i.equals(null));
	}

	@ParameterizedTest
	@CsvSource({ "111", "222", "333" })
	void shouldNotFindItemByIdParametrized(final Integer itemId) {
		Item i = this.itemService.findItemById(itemId);
		Assertions.assertTrue(i == null);
	}

}
