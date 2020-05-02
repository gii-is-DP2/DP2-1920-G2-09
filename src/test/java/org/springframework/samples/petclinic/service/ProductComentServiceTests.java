package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductComentServiceTests {

	@Autowired
	protected ProductComentService productComentService;

	@Autowired
	protected ProductService productService;

	@Autowired
	protected VetService vetService;

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
		u.setEmail("email@bien.com");
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
	void shouldGetRatingOfTheProduct() {
		Double rating = this.productComentService.getAverageRatingOfProduct(1);
		Assert.assertTrue(rating > 0.0);
	}

	@Test
	void shouldNotGetRatingOfTheProduct() {
		Double rating = this.productComentService.getAverageRatingOfProduct(1231231);
		Assert.assertTrue(rating == 0.0);
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
		u.setEmail("email@bien.com");
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

		// Comprobamos que salta el error a la hora de intentar borrar un comentario con
		// una id que no existe
		Assertions.assertThrows(EmptyResultDataAccessException.class,
				() -> this.productComentService.deleteProductComent(25));

	}

	// PRUEBAS PARAMETRIZADAS
	@ParameterizedTest
	@CsvSource({ "prueba", "admin1", "owner1", "vet1" })
	void shouldFindUserByUsernameParametrized(final String username) {
		User user = this.productComentService.findUserByUsername(username);
		Assertions.assertTrue(user != null);

	}

	@ParameterizedTest
	@CsvSource({
			"producto1,http://www.urldeprueba.com,Descripcion 1, ACCESORY, 10.20, 20, usernuevo, passwordnueva, email@gmail.com, NuevaDescripcionComent, 5, NuevoTituloComent ",
			"producto2,http://www.urldeprueba2.com,Descripcion 2, FOOD, 10.00, 2000000, usernuevo2, passwordnueva2, email2@gmail.com, NuevaDescripcionComent2, 0, NuevoTituloComent2 ",
			"producto3,http://www.urldeprueba3.com,Descripcion 3, ACCESORY, 1000.20, 20, usernuevo3, passwordnueva3, email3@gmail.com, NuevaDescripcionComent, 2, NuevoTituloComent3 " })
	void shouldDeleteProductComentParametrized(final String productName, final String productUrlImage,
			final String productDescription, final Category productCategory, final Double productUnitPrice,
			final Integer productStock, final String username, final String password, final String email,
			final String productComentDescription, final Integer rating, final String productComentTitle) {
		ProductComent pC = new ProductComent();
		Product p = new Product();
		p.setName(productName);
		p.setUrlImage(productUrlImage);
		p.setDescription(productDescription);
		p.setCategory(productCategory);
		p.setUnitPrice(productUnitPrice);
		p.setStock(productStock);
		User u = new User();
		u.setEnabled(true);
		u.setPassword(password);
		u.setUsername(username);
		u.setEmail(email);
		pC.setDescription(productComentDescription);
		pC.setHighlight(false);
		pC.setPostDate(LocalDate.now().minusDays(2));
		pC.setProduct(p);
		pC.setRating(rating);
		pC.setTitle(productComentTitle);
		pC.setUser(u);
		this.productComentService.saveProductComent(pC);

		Assert.assertTrue(pC.getId().longValue() != 0);

		this.productComentService.deleteProductComent(pC.getId());
		Assert.assertTrue(this.productComentService.findAllComentsOfTheProduct(p.getId()).size() == 0);

	}

	@ParameterizedTest
	@CsvSource({
			"producto1,http://www.urldeprueba.com,Descripcion 1, ACCESORY, 10.20, 20, usernuevo, passwordnueva, email@gmail.com, NuevaDescripcionComent, 5, NuevoTituloComent ",
			"producto2,http://www.urldeprueba2.com,Descripcion 2, FOOD, 10.00, 2000000, usernuevo2, passwordnueva2, email2@gmail.com, NuevaDescripcionComent2, 0, NuevoTituloComent2 ",
			"producto3,http://www.urldeprueba3.com,Descripcion 3, ACCESORY, 1000.20, 20, usernuevo3, passwordnueva3, email3@gmail.com, NuevaDescripcionComent, 2, NuevoTituloComent3 " })
	void shouldInsertProductComentParametrized(final String productName, final String productUrlImage,
			final String productDescription, final Category productCategory, final Double productUnitPrice,
			final Integer productStock, final String username, final String password, final String email,
			final String productComentDescription, final Integer rating, final String productComentTitle) {
		ProductComent pC = new ProductComent();
		Product p = new Product();
		p.setName(productName);
		p.setUrlImage(productUrlImage);
		p.setDescription(productDescription);
		p.setCategory(productCategory);
		p.setUnitPrice(productUnitPrice);
		p.setStock(productStock);
		User u = new User();
		u.setEnabled(true);
		u.setPassword(password);
		u.setUsername(username);
		u.setEmail(email);
		pC.setDescription(productComentDescription);
		pC.setHighlight(false);
		pC.setPostDate(LocalDate.now().minusDays(2));
		pC.setProduct(p);
		pC.setRating(rating);
		pC.setTitle(productComentTitle);
		pC.setUser(u);
		this.productComentService.saveProductComent(pC);
		Assert.assertTrue(pC.getId().longValue() != 0);
		Assert.assertTrue(this.productComentService.findAllComentsOfTheProduct(p.getId()).size() == 1);

	}

	@ParameterizedTest
	@CsvSource({ "1" })
	void shouldGetRatingOfTheProductParamtrized(final Integer productId) {
		Double rating = this.productComentService.getAverageRatingOfProduct(productId);
		Assert.assertTrue(rating > 0.0);
	}
}
