
package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Service;

@DisplayName("Products Service Tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ProductServiceTests {

	@Autowired
	protected ProductService productService;


	@Test
	void shouldFindProducts() {
		Iterable<Product> products = this.productService.findAllProducts();
		Assertions.assertThat(products).isNotEmpty();
	}

	@Test
	void shouldFindProductsByName() {
		Iterable<Product> products = this.productService.findFilteredProducts("Gel de perro");
		Assertions.assertThat(products).isNotEmpty();
	}

	@Test
	void shouldNotFindProductsByName() {
		Iterable<Product> products = this.productService.findFilteredProducts("Producto que no existe");
		Assertions.assertThat(products).isEmpty();
	}

}
