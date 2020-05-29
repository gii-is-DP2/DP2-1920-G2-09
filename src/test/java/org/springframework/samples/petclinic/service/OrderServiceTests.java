
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderServiceTests {

	@Autowired
	protected OrderService	orderService;

	@Autowired
	protected OwnerService	ownerService;


	@Test
	void shouldSaveOrder() throws DuplicatedUsernameException {
		Integer before = this.orderService.findAllOrders().size();
		Order o = new Order();
		o.setId(24);
		o.setOrderDate(LocalDate.now());
		o.setTotalPrice(100.0);
		Owner owner = new Owner();
		owner.setFirstName("Sam2");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		User user = new User();
		user.setUsername("Sam2");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		owner.setUser(user);
		this.ownerService.saveOwner(owner);
		o.setOwner(owner);
		this.orderService.saveOrder(o);
		Integer after = this.orderService.findAllOrders().size();
		Assertions.assertTrue(before + 1 == after);
	}

	@Test
	void shouldFindOrders() {
		Collection<Order> orders = this.orderService.findAllOrders();
		Assertions.assertTrue(!orders.isEmpty());
	}

	@Test
	void shouldFindOrdersOrderedByDate() {
		List<Order> orders = this.orderService.findAllOrdersOrderedByDate();
		Assertions.assertTrue(orders.get(0).getOrderDate().isAfter(orders.get(orders.size() - 1).getOrderDate()));
	}

	@Test
	void shouldFindOrderById() {
		Order order = this.orderService.findOrderById(1);
		Assertions.assertTrue(order != null);
	}

	@Test
	void shouldFindItemsByOrder() {
		List<Item> items = this.orderService.findAllItemByOrder(1);
		Assertions.assertTrue(!items.isEmpty());
	}

	@Test
	void shouldDeleteOrder() {
		int before = this.orderService.findAllOrders().size();
		this.orderService.deleteOrder(2);
		int after = this.orderService.findAllOrders().size();
		Assertions.assertTrue(before == after + 1);
	}

	@Test
	void shouldFindAllOrdersByOwner() {
		List<Order> orders = this.orderService.findAllOrdersByOwner("owner1");
		Assertions.assertTrue(!orders.isEmpty());
	}

	//PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({
		"Juan,Romero,Calle Andaluz,Sevilla,901123123,username1,pwd1,email@gmail.com,1,100.0", "Pedro,San Juan,Calle Pacense,Badajoz,925646123,username2,pwd2,email2@gmail.com,2,101.0",
		"Andrea,Pérez,Calle Rio Bajo,Zaragoza,900110023,username3,pwd3,email3@gmail.com,3,102.0"
	})
	void shouldSaveOrderParametrized(final String name, final String lastName, final String address, final String city, final String telephone, final String username, final String password, final String email, final Integer orderId, final Double price)
		throws DuplicatedUsernameException {

		Order o = new Order();
		o.setId(orderId);
		o.setOrderDate(LocalDate.now());
		o.setTotalPrice(price);
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
		this.ownerService.saveOwner(owner);
		o.setOwner(owner);
		this.orderService.saveOrder(o);

		Assertions.assertTrue(this.orderService.findAllOrders().contains(o));
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldFindOrderById(final Integer id) {
		Order order = this.orderService.findOrderById(id);
		Assertions.assertTrue(order != null);
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldFindItemsByOrder(final Integer id) {
		List<Item> items = this.orderService.findAllItemByOrder(id);
		Assertions.assertTrue(!items.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldDeleteOrder(final Integer id) {
		int before = this.orderService.findAllOrders().size();
		this.orderService.deleteOrder(id);
		int after = this.orderService.findAllOrders().size();
		Assertions.assertTrue(before == after + 1);
	}

	@ParameterizedTest
	@CsvSource({
		"prueba", "owner1", "owner2"
	})
	void shouldFindAllOrdersByOwner(final String username) {
		List<Order> orders = this.orderService.findAllOrdersByOwner(username);
		Assertions.assertTrue(!orders.isEmpty());
	}

}
