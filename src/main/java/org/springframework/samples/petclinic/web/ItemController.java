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

		Product p = this.productService.findProductById(productId);

		if (item.getQuantity() > p.getStock() || result.hasErrors()) {
			model.addAttribute("errorMessage", "The quantity selected is greather than the stock");
			return new ProductController(this.productService, this.productComentService).showProduct(productId, model);
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();
			UserDetails us = null;
			if (principal instanceof UserDetails) {
				us = (UserDetails) principal;
			}
			ShoppingCart sp = this.shoppingCartService.getShoppingCartOfUser(us.getUsername());

			item.setUnitPrice(p.getUnitPrice());
			item.setShoppingCart(sp);
			item.setProduct(p);

			this.itemService.saveItem(item);

			p.setStock(p.getStock() - item.getQuantity());
			this.productService.saveProduct(p);
			return "redirect:/products/{productId}";
		}

	}

}
