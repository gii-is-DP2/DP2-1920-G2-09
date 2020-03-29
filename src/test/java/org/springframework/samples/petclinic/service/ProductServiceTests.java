
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Service;

@DisplayName("Products Service Tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductServiceTests {

	@Autowired
	protected ProductService productService;

	@Test
	void shouldFindProducts() {
		Collection<Product> products = (Collection<Product>) this.productService.findAllProducts();
		Assertions.assertTrue(!products.isEmpty());
	}

	@Test
	void shouldFindProductsByName() {
		Collection<Product> products = (Collection<Product>) this.productService.findFilteredProducts("Gel de perro");
		Assertions.assertTrue(!products.isEmpty());
	}

	@Test
	void shouldNotFindProductsByName() {
		Collection<Product> products = (Collection<Product>) this.productService
				.findFilteredProducts("Producto que no existe");
		Assertions.assertTrue(products.isEmpty());
	}

	@Test
	void shouldFindProductById() {
		Product product = this.productService.findProductById(1);
		Assertions.assertTrue(product != null);
	}

	@Test
	void shouldNotFindProductById() {
		Product product = this.productService.findProductById(-1);
		Assertions.assertTrue(product == null);
	}

	@Test
	void shouldInsertProduct() {
		Collection<Product> products = (Collection<Product>) this.productService
				.findFilteredProducts("Producto de prueba");
		int found = products.size();
		Product product = new Product();
		product.setName("Producto de prueba");
		product.setUrlImage("http://www.urldeprueba.com");
		product.setDescription("Descripción de prueba");
		product.setCategory(Category.ACCESORY);
		product.setUnitPrice(10.20);
		product.setStock(20);

		this.productService.saveProduct(product);
		Assert.assertTrue(product.getId().longValue() != 0);

		products = (Collection<Product>) this.productService.findFilteredProducts("Producto de prueba");
		Assert.assertTrue(products.size() == found + 1);
	}

	@Test
	void shouldNotInsertProduct() {
		Product product = new Product();
		Assertions.assertThrows(ConstraintViolationException.class, () -> this.productService.saveProduct(product));

	}

	// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "gel de perro", "GEL DE GATO", "gel DE caBAllO" })
	void shouldFindProductsByNameParametrized(final String search) {
		Collection<Product> products = (Collection<Product>) this.productService.findFilteredProducts(search);
		Assertions.assertTrue(!products.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "este producto no existe", "esteotroproductotampocoexiste",
			" CREO     QUE    ESTE TAMPOCO    EXISTE" })
	void shouldNotFindProductsByNameParametrized(final String search) {
		Collection<Product> products = (Collection<Product>) this.productService.findFilteredProducts(search);
		Assertions.assertTrue(products.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "1", "2", "3" })
	void shouldFindProductByIdParametrized(final Integer productId) {
		Product product = this.productService.findProductById(productId);
		Assertions.assertTrue(product != null);
	}

	@ParameterizedTest
	@CsvSource({ "0", "2222", "33333" })
	void shouldNotFindProductByIdParametrized(final Integer productId) {
		Product product = this.productService.findProductById(productId);
		Assertions.assertTrue(product == null);
	}

	@ParameterizedTest
	@CsvSource({ "nombreDeProducto1, https://tinyurl.com/vp4wlrz, descripcion1, FOOD, 10.20, 50",
			"nombreDeProducto2, https://tinyurl.com/vp4wlrzASDASD, descripcion2, ACCESORY, 100.00, 1",
			"nombreDeProducto3, https://tinyurl.com/NUEVARUTA, descripcion3, MEDICAMENT, 1.00, 900000" })
	void shouldInsertProductParametrized(final String productName, final String productImageUrl,
			final String description, final Category category, final Double unitPrice, final Integer stock) {
		Collection<Product> products = (Collection<Product>) this.productService.findFilteredProducts(productName);
		int found = products.size();
		Product product = new Product();
		product.setName(productName);
		product.setUrlImage(productImageUrl);
		product.setDescription(description);
		product.setCategory(category);
		product.setUnitPrice(unitPrice);
		product.setStock(stock);

		this.productService.saveProduct(product);
		Assert.assertTrue(product.getId().longValue() != 0);

		products = (Collection<Product>) this.productService.findFilteredProducts(productName);
		Assert.assertTrue(products.size() == found + 1);
	}

}
