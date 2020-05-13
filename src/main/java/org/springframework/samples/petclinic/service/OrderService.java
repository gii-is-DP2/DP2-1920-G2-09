
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private OrderRepository orderRepository;

	@Autowired
	public OrderService(final OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@Transactional
	public void saveOrder(final Order order) {
		this.orderRepository.save(order);
	}

	@Transactional
	public List<Order> findAllOrders() {
		return (List<Order>) this.orderRepository.findAll();
	}

	@Transactional
	public List<Order> findAllOrdersOrderedByDate() {
		return this.orderRepository.findAllOrderedByDate();
	}

	@Transactional
	public Order findOrderById(final int id) {
		return this.orderRepository.findById(id).get();
	}

	@Transactional
	public List<Item> findAllItemByOrder(final int id) {
		return this.orderRepository.findAllItemByOrder(id);
	}

	@Transactional
	public void deleteOrder(final int id) {
		this.orderRepository.deleteAllItemsOfOrder(id);
		this.orderRepository.deleteById(id);

	}

}
