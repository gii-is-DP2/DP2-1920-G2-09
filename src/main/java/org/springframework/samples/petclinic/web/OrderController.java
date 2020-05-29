
package org.springframework.samples.petclinic.web;

import java.util.List;

import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.springdatajpa.OwnerCrudRepository;
import org.springframework.samples.petclinic.service.ItemService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orders")
public class OrderController {

	private ItemService			itemService;
	private OrderService		orderService;
	private OwnerService		ownerService;

	private OwnerCrudRepository	ownerCrudRepository;


	public OrderController(final ItemService itemService, final OrderService orderService) {
		super();
		this.itemService = itemService;
		this.orderService = orderService;
	}

	@ModelAttribute("item")
	public Item initiateItem() {
		Item i = new Item();
		i.setQuantity(1);
		return i;
	}

	@GetMapping("/list")
	public String showAllOrder(final ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		List<Order> orders = this.orderService.findAllOrdersByOwner(us.getUsername());
		model.put("orders", orders);
		return "orders/ordersList";
	}

	@GetMapping("/{orderId}")
	public String showOrder(final ModelMap model, @PathVariable("orderId") final int orderId) {
		Order order = this.orderService.findOrderById(orderId);
		List<Item> items = this.orderService.findAllItemByOrder(orderId);
		model.addAttribute("order", order);
		model.addAttribute("items", items);
		return "orders/ordersDetails";
	}

	@GetMapping("/delete/{orderId}")
	public String deleteOrder(final ModelMap model, @PathVariable("orderId") final int orderId) {
		this.orderService.deleteOrder(orderId);
		String okMessage = "The order has been canceled";
		model.put("okMessage", okMessage);
		return this.showAllOrder(model);
	}

}
