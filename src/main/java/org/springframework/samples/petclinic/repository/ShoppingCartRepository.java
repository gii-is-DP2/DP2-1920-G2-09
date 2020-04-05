package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Integer> {

	@Query("Select sp from ShoppingCart sp where sp.owner.user.username = ?1")
	ShoppingCart getShoppingCartOfUser(String username);

}
