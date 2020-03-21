package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductComentController {

    private final ProductComentService productComentService;
    private final ProductService productService;

    @Autowired
    public ProductComentController(final ProductComentService productComentService,
	    final ProductService productService) {
	this.productComentService = productComentService;
	this.productService = productService;

    }

    @PostMapping("products/{productId}/add-product-coment")
    public String saveProductComent(@PathVariable("productId") final int productId,
	    @Valid final ProductComent productComent, final BindingResult result, final ModelMap model) {
	ProductComentValidador p = new ProductComentValidador();
	Errors errors = new BeanPropertyBindingResult(productComent, "productComent");
	p.validate(productComent, errors);
	if (errors.hasErrors()) {
	    model.put("productComent", productComent);
	    result.addAllErrors(errors);
	    return new ProductController(this.productService, this.productComentService).showProduct(productId, model);
	} else {
	    Product product = this.productService.findProductById(productId);

	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    Object principal = auth.getPrincipal();
	    UserDetails us = null;
	    if (principal instanceof UserDetails) {
		us = (UserDetails) principal;
	    }

	    User user = this.productComentService.findUserByUsername(us.getUsername());
	    productComent.setProduct(product);
	    productComent.setUser(user);
	    productComent.setHighlight(false);
	    productComent.setPostDate(LocalDate.now());
	    this.productComentService.saveProductComent(productComent);
	    model.addAttribute("OKmessage", "Your comment have been submited correctly");
	    return new ProductController(this.productService, this.productComentService).showProduct(productId, model);
	}

    }
    
    @GetMapping("products/{productId}/delete-product-coment/{productComentId}")
    public String deleteProductComent(@PathVariable("productComentId") final int productComentId,@PathVariable("productId") final int productId, final ModelMap model) {
    	
    	
		this.productComentService.deleteProductComent(productComentId);
    	model.addAttribute("OKDeletemessage",  "El comentario se ha Eliminado Correctamente");

    	
		return new ProductController(this.productService, this.productComentService).showProduct(productId, model);
    	
    }
    
    
}
