
package org.springframework.samples.petclinic.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.ProductValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class ProductValidatorTests {

	private Product product;

	@BeforeEach
	void setInitialProduct() {
		Product p = new Product();
		p.setName("Nombre");
		p.setUrlImage("http://www.urldeprueba.com");
		p.setDescription("Descripción de prueba");
		p.setCategory(Category.ACCESORY);
		p.setUnitPrice(10.20);
		p.setStock(20);
		p.setAvailable(true);
		this.product = p;
	}

	@Test
	void shouldValidateProductWithoutErrors() {
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenNameEmpty() {
		this.product.setName("");
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("The name of the object cannot be empty");
	}

	@Test
	void shouldNotValidateDescriptionIsLessThan20Characters() {
		this.product.setDescription("menos de 20");
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("The length of the description must be between 20 and 128 characters");
	}

	@Test
	void shouldNotValidateDescriptionIsLongerThan128Characters() {
		this.product.setDescription(
				"ESTA DESCRIPCIÓN ES DEMASIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADO LARGA");
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("The length of the description must be between 20 and 128 characters");
	}

	@Test
	void shouldNotValidateStockLessThan0() {
		this.product.setStock(-20);
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("stock").getCode())
				.isEqualTo("The minimum stock possible is 0 units");
	}

	@Test
	void shouldNotValidateNegativeUnitPrice() {
		this.product.setUnitPrice(-20.20);
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("unitPrice").getCode())
				.isEqualTo("The price of the product must be more than 0 euros");
	}

	@Test
	void shouldNotValidateZeroUnitPrice() {
		this.product.setUnitPrice(0.0);
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("unitPrice").getCode())
				.isEqualTo("The price of the product must be more than 0 euros");
	}

	@Test
	void shouldNotValidateEmptyCategory() {
		this.product.setCategory(null);
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("category").getCode()).isEqualTo("You must select one category");
	}

	@Test
	void shouldNotValidateZeroStockAndAvailable() {
		this.product.setStock(0);
		ProductValidator pv = new ProductValidator();
		Errors errors = new BeanPropertyBindingResult(this.product, "");
		pv.validate(this.product, errors);
		Assertions.assertThat(errors.getFieldError("stock").getCode())
				.isEqualTo("The product must have stock to be available");
	}
}
