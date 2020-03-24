
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductComentServiceTest {

	@Autowired
	protected ProductComentService productComentService;


	@Test
	void shouldFindProductComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsOfTheProduct(1);
		Assertions.assertTrue(!productComents.isEmpty());
	}

	@Test
	void shouldNotFindProductComents() {
		Collection<ProductComent> productComents = this.productComentService.findAllComentsOfTheProduct(-1);
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
		Product p = new Product();
		p.setName("Producto de prueba");
		p.setUrlImage("http://www.urldeprueba.com");
		p.setDescription("Descripción de prueba");
		p.setCategory(Category.ACCESORY);
		p.setUnitPrice(10.20);
		p.setStock(20);
		User u = new User();
		u.setEnabled(true);
		u.setPassword("contraseña");
		u.setUsername("u");
		pC.setDescription("Descripcion de prueba");
		pC.setHighlight(false);
		pC.setPostDate(LocalDate.now().minusDays(2));
		pC.setProduct(p);
		pC.setRating(4);
		pC.setTitle("Esto es un titulo");
		pC.setUser(u);
		this.productComentService.saveProductComent(pC);
		Assert.assertTrue(pC.getId().longValue() != 0);
		Assert.assertTrue(this.productComentService.findAllComentsOfTheProduct(p.getId()).size() == 1);
	}

	@Test
	void shouldDeleteProductComent() {
		ProductComent pC = new ProductComent();
		Product p = new Product();
		p.setName("Producto de prueba");
		p.setUrlImage("http://www.urldeprueba.com");
		p.setDescription("Descripción de prueba");
		p.setCategory(Category.ACCESORY);
		p.setUnitPrice(10.20);
		p.setStock(20);
		User u = new User();
		u.setEnabled(true);
		u.setPassword("contraseña");
		u.setUsername("u");
		pC.setDescription("Descripcion de prueba");
		pC.setHighlight(false);
		pC.setPostDate(LocalDate.now().minusDays(2));
		pC.setProduct(p);
		pC.setRating(4);
		pC.setTitle("Esto es un titulo");
		pC.setUser(u);
		this.productComentService.saveProductComent(pC);
		
		Assert.assertTrue(pC.getId().longValue() != 0);
		
		this.productComentService.deleteProductComent(pC.getId());
		Assert.assertTrue(this.productComentService.findAllComentsOfTheProduct(p.getId()).size() == 0);
	}
	
	
	@Test
	void shouldNotDeleteProductComent() {
		
		//Comprobamos que salta el error a la hora de intentar borrar un comentario con una id que no existe
	Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.productComentService.deleteProductComent(15));
		
		
	}
	
}
