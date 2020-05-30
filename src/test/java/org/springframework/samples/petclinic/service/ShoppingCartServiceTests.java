
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartServiceTests {

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

	// PRUEBAS PARAMETRIZADAS
	@ParameterizedTest
	@CsvSource({
		"Juan,Romero,Calle Andaluz,Sevilla,901123123,username1,pwd1,email@gmail.com,1", "Pedro,San Juan,Calle Pacense,Badajoz,925646123,username2,pwd2,email2@gmail.com,2", "Andrea,Pérez,Calle Rio Bajo,Zaragoza,900110023,username3,pwd3,email3@gmail.com,3"
	})
	void shouldSaveShoppingCartParametrized(final String name, final String lastName, final String address, final String city, final String telephone, final String username, final String password, final String email, final Integer shoppingCartId) {
		ShoppingCart shoppingCart = new ShoppingCart();
		Owner owner = new Owner();
		owner.setFirstName(name);
		owner.setLastName(lastName);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		user.setEmail(email);
		owner.setUser(user);
		shoppingCart.setOwner(owner);
		shoppingCart.setId(shoppingCartId);
		this.shoppingCartService.saveShoppingCart(shoppingCart);
		Assertions.assertTrue(!this.shoppingCartService.getShoppingCartOfUser(username).equals(null));
	}

	@ParameterizedTest
	@CsvSource({
		"prueba", "owner1", "owner10"
	})
	void shouldGetShoppingCartOfUserParametrized(final String username) {
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser(username);
		Assertions.assertTrue(sp != null);
	}

	@ParameterizedTest
	@CsvSource({
		"esteUsuarioNoExiste", "esteOtroTampoco", "yElTerceroTampoco"
	})
	void shouldNotGetShoppingCartOfUserParametrized(final String username) {
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser(username);
		Assertions.assertTrue(sp == null);
	}

}
