
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
class OrderServiceTests {

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected OwnerService ownerService;

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

}
