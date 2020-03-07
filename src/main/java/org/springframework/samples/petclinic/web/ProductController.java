
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;


	@Autowired
	public ProductController(final ProductService productService) {
		this.productService = productService;
	}

	@GetMapping(value = "/all")
	public String findAllProducts(final ModelMap model) {
		Product p = new Product();
		model.put("product", p);
		Iterable<Product> products = this.productService.findAllProducts();
		model.addAttribute("products", products);
		return "products/listProducts";
	}

	@GetMapping(value = "/filter")
	public String findProductsFiltered(final ModelMap model, final Product product) {
		model.put("product", product);
		Iterable<Product> products = this.productService.findFilteredProducts(product.getName());
		model.addAttribute("products", products);
		return "products/listProducts";
	}

}
