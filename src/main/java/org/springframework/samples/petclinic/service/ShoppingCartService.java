package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShoppingCartService {

	private ShoppingCartRepository shoppingCartRepository;

	@Autowired
	public ShoppingCartService(final ShoppingCartRepository shoppingCartRepository) {
		this.shoppingCartRepository = shoppingCartRepository;
	}

	@Transactional
	public void saveShoppingCart(final ShoppingCart shoppingCart) {
		this.shoppingCartRepository.save(shoppingCart);
	}

	@Transactional
	public ShoppingCart getShoppingCartOfUser(final String username) {
		return this.shoppingCartRepository.getShoppingCartOfUser(username);
	}
}
