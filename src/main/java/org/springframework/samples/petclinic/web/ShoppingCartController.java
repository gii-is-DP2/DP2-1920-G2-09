package org.springframework.samples.petclinic.web;

import java.util.List;

import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.service.ItemService;
import org.springframework.samples.petclinic.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	private ShoppingCartService shoppingCartService;
	private ItemService itemService;

	public ShoppingCartController(final ShoppingCartService shoppingCartService, final ItemService itemService) {
		super();
		this.shoppingCartService = shoppingCartService;
		this.itemService = itemService;
	}

	@ModelAttribute("item")
	public Item initiateItem() {
		Item i = new Item();
		i.setQuantity(1);
		return i;
	}

	@GetMapping("")
	public String showShoppingCartOfOwner(final ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser(us.getUsername());
		List<Item> items = this.itemService.findItemsInShoppingCart(sp.getId());
		model.put("items", items);
		return "/owners/ownerShoppingCart";

	}

}
