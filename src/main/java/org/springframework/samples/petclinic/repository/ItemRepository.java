package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Item;

public interface ItemRepository extends CrudRepository<Item, Integer> {

	@Query("Select i from Item i where i.shoppingCart.id = ?1")
	List<Item> findItemsInShoppingCard(int shoppingCartId);
}
