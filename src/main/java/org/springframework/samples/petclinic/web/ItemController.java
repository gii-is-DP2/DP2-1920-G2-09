package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.service.ItemService;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShoppingCartService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ItemController {

	private ItemService itemService;
	private ShoppingCartService shoppingCartService;
	private ProductService productService;
	private ProductComentService productComentService;

	@Autowired
	public ItemController(final ItemService itemService, final ShoppingCartService shoppingCartService,
			final ProductService productService, final ProductComentService productComentService) {
		this.itemService = itemService;
		this.shoppingCartService = shoppingCartService;
		this.productService = productService;
		this.productComentService = productComentService;
	}

	@PostMapping(value = "/add-item/{productId}")
	public String addItemToShoppingCart(@PathVariable("productId") final int productId, final Item item,
			final BindingResult result, final ModelMap model) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser(us.getUsername());

		Product p = this.productService.findProductById(productId);

		if (item.getQuantity() > p.getStock() || result.hasErrors()) {
			model.addAttribute("errorMessage", "The quantity selected is greather than the stock");
			return new ProductController(this.productService, this.productComentService).showProduct(productId, model);
		} else {
			Item itemInCart = this.itemService.checkIfItemIsIntheShoppingCart(sp.getId(), p.getId());

			if (itemInCart == null) {
				item.setUnitPrice(p.getUnitPrice());
				item.setShoppingCart(sp);
				item.setProduct(p);
				this.itemService.saveItem(item);
			} else {
				itemInCart.setQuantity(itemInCart.getQuantity() + item.getQuantity());
				this.itemService.saveItem(itemInCart);
			}

			p.setStock(p.getStock() - item.getQuantity());

			this.productService.saveProduct(p);
			return "redirect:/products/{productId}";
		}

	}

	@GetMapping(value = "/delete-item/{itemId}")
	public String deleteItemFromShoppingCart(@PathVariable("itemId") final int itemId, final ModelMap model) {
		Item item = this.itemService.findItemById(itemId);
		Product p = item.getProduct();
		p.setStock(p.getStock() + item.getQuantity());
		if (!p.isAvailable()) {
			p.setAvailable(true);
		}
		this.productService.saveProduct(p);
		this.itemService.deleteItem(itemId);

		return "redirect:/shopping-cart";

	}

	@PostMapping(value = "/edit-item/{itemId}")
	public String editItemQuantity(@PathVariable("itemId") final int itemId, final ModelMap model, final Item item,
			final BindingResult result) {
		Integer updatedQuantity = item.getQuantity();
		Item itemToUpdate = this.itemService.findItemById(itemId);
		Product productToUpdate = itemToUpdate.getProduct();

		if (updatedQuantity > productToUpdate.getStock() + itemToUpdate.getQuantity()) {
			model.addAttribute("errorMessage", "The quantity selected is greather than the stock");
			return "redirect:/shopping-cart";
		} else {

			productToUpdate.setStock(productToUpdate.getStock() + itemToUpdate.getQuantity() - updatedQuantity);
			itemToUpdate.setQuantity(updatedQuantity);
			this.productService.saveProduct(productToUpdate);
			this.itemService.saveItem(itemToUpdate);
			return "redirect:/shopping-cart";
		}

	}

}
