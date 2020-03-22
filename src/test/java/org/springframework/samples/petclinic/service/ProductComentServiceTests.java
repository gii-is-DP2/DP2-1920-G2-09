
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductComentServiceTests {

	@Autowired
	protected ProductComentService productComentService;
	
	@Autowired
	protected ProductService productService;
	
	@Autowired
	protected VetService  vetService;


	@Test
	void shouldFindProductOwnerComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsOwnersOfTheProduct(1);
		Assertions.assertTrue(!productComents.isEmpty());
	}

	@Test
	void shouldNotFindProductOwnerComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsOwnersOfTheProduct(-1);
		Assertions.assertTrue(productComents.isEmpty());
	}
	
	@Test
	void shouldFindProductVetComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsVetsOfTheProduct(1);
		Assertions.assertTrue(!productComents.isEmpty());
	}

	@Test
	void shouldNotFindProductVetComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsVetsOfTheProduct(-1);
		Assertions.assertTrue(productComents.isEmpty());
	}

	@Test
	void shouldFindUserByUsername() {
		User user = this.productComentService.findUserByUsername("prueba");
		Assertions.assertTrue(user != null);

	}

	@Test
	void shouldNotFindUserByUsername() {
		User user = this.productComentService.findUserByUsername("");
		Assertions.assertTrue(user == null);

	}
	
	@Test
	void shouldInsertProductComent() {
		
		ProductComent pC = new ProductComent();
		Product p = this.productService.findProductById(1);
		Integer found = this.productComentService.findAllComentsOwnersOfTheProduct(p.getId()).size()
				+ this.productComentService.findAllComentsVetsOfTheProduct(p.getId()).size();
		
		pC.setDescription("Descripcion de prueba");
		pC.setHighlight(false);
		pC.setPostDate(LocalDate.now().minusDays(2));
		pC.setProduct(p);
		pC.setRating(4);
		pC.setTitle("Esto es un titulo");
		pC.setUser(this.vetService.findVetById(1).getUser());
		
		this.productComentService.saveProductComent(pC);
		
		Assert.assertTrue(pC.getId().longValue() != 0);
		Assert.assertTrue(this.productComentService.findAllComentsOwnersOfTheProduct(p.getId()).size()
				+ this.productComentService.findAllComentsVetsOfTheProduct(p.getId()).size() == found + 1 );
	}
}
