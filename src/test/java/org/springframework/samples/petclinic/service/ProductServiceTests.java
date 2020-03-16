
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

}
