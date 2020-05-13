package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
	List<Order> findAllOrderedByDate();
	
	@Query("SELECT i FROM Item i WHERE i.order.id = ?1")
	List<Item> findAllItemByOrder(int orderId);
	
}
