
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	private final ProductComentService productComentService;
	private final String PRODUCT = "product";
	private final String PRODUCTS = "products";
	private final String PRODUCT_COMMENT = "productComent";

	@Autowired
	public ProductController(final ProductService productService, final ProductComentService productComentService) {
		this.productService = productService;
		this.productComentService = productComentService;
	}

	@InitBinder("product")
	public void initProductBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new ProductValidator());
	}

	@ModelAttribute("categories")
	public List<String> populatePetTypes() {
		List<String> res = new ArrayList<>();
		for (Category c : Category.values()) {
			res.add(c.toString());
		}
		return res;
	}

	@ModelAttribute("item")
	public Item initiateItem() {
		Item i = new Item();
		i.setQuantity(1);
		return i;
	}

	@GetMapping(value = "/all")
	public String findAllProducts(final ModelMap model) {
		Product p = new Product();
		model.put(this.PRODUCT, p);
		Iterable<Product> products = this.productService.findAllProducts();
		model.addAttribute(this.PRODUCTS, products);
		return "products/listProducts";
	}

	@GetMapping(value = "/filter")
	public String findProductsFiltered(final ModelMap model, final Product product) {
		model.put(this.PRODUCT, product);
		Iterable<Product> products = this.productService.findFilteredProducts(product.getName());
		List<Product> l = new ArrayList<>();
		for (Product p : products) {
			l.add(p);
		}
		if (l.isEmpty()) {
			model.addAttribute("noItemsMessage", "There aren't products for this search");
		}
		model.addAttribute(this.PRODUCTS, products);
		return "products/listProducts";
	}

	@GetMapping(value = "/filterByCategory")
	public String findProductsFilteredByCategory(final ModelMap model, final Product product) {
		model.put(this.PRODUCT, product);
		Iterable<Product> products = this.productService.findFilteredProductsByCategory(product.getCategory());
		List<Product> l = new ArrayList<>();
		for (Product p : products) {
			l.add(p);
		}
		if (l.isEmpty()) {
			model.addAttribute("noItemsMessage", "There aren't products for this search");
		}
		model.addAttribute(this.PRODUCTS, products);
		return "products/listProducts";
	}

	@GetMapping(value = "/{productId}")
	public String showProduct(@PathVariable("productId") final int productId, final ModelMap model) {
		ProductComent pc = new ProductComent();
		Collection<ProductComent> coments = this.productComentService.findAllComentsOfTheProduct(productId);
		if (!model.containsAttribute("OKmessage") && model.containsAttribute(this.PRODUCT_COMMENT)) {
			model.put(this.PRODUCT_COMMENT, model.get(this.PRODUCT_COMMENT));

		} else {
			model.put(this.PRODUCT_COMMENT, pc);

		}
		Double rating = this.productComentService.getAverageRatingOfProduct(productId);
		model.put("rating", rating);
		model.put("coments", coments);
		Product product = this.productService.findProductById(productId);
		if (product == null) {
			return "exception";
		} else {
			model.put(this.PRODUCT, product);
			return "products/productDetails";
		}

	}

	@GetMapping(value = "/new")
	public String initCreationForm(final Product owner, final ModelMap model) {
		Product product = new Product();
		model.put(this.PRODUCT, product);
		return "products/createOrUpdateProductForm";
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Product product, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put(this.PRODUCT, product);
			return "products/createOrUpdateProductForm";
		} else {
			this.productService.saveProduct(product);
			return this.findAllProducts(model);
		}
	}

	@GetMapping(value = "/{productId}/edit")
	public String initUpdateProductForm(@PathVariable("productId") final int productId, final Model model) {
		Product product = this.productService.findProductById(productId);
		model.addAttribute(this.PRODUCT, product);
		return "products/createOrUpdateProductForm";
	}

	@PostMapping(value = "/{productId}/edit")
	public String processUpdateProductForm(@Valid final Product product, final BindingResult result, final Model model,
			@PathVariable("productId") final int productId) {
		if (result.hasErrors()) {
			model.addAttribute(this.PRODUCT, product);
			return "products/createOrUpdateProductForm";
		} else {
			product.setId(productId);
			this.productService.saveProduct(product);
			return "redirect:/products/{productId}";
		}
	}

}
