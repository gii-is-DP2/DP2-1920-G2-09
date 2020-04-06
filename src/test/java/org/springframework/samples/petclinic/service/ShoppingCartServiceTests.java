
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ShoppingCartServiceTests {

	@Autowired
	protected ShoppingCartService shoppingCartService;


	@Test
	void shouldSaveShoppingCart() {
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
		shoppingCart.setId(3);
		this.shoppingCartService.saveShoppingCart(shoppingCart);
		Assertions.assertTrue(!this.shoppingCartService.getShoppingCartOfUser("Sam").equals(null));
	}

	@Test
	void shouldGetShoppingCartOfUser() {
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser("prueba");
		Assertions.assertTrue(sp != null);
	}

	@Test
	void shouldNotGetShoppingCartOfUser() {
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser("AHHHHHHHHHHHHHHH");
		Assertions.assertTrue(sp == null);
	}

}
