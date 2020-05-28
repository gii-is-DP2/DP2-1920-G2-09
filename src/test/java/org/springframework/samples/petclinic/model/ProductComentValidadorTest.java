
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.ProductComentValidador;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class ProductComentValidadorTest {

	private ProductComent productComent;

	@BeforeEach
	void setInitialProductComent() {
		ProductComent pc = new ProductComent();
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
		pc.setDescription("Descripcion de prueba");
		pc.setHighlight(false);
		pc.setPostDate(LocalDate.now().minusDays(2));
		pc.setProduct(p);
		pc.setRating(4);
		pc.setTitle("Esto es un titulo");
		pc.setUser(u);
		this.productComent = pc;
	}

	@Test
	void shouldValidateProductComentWithoutErrors() {
		ProductComentValidador pcv = new ProductComentValidador();
		Errors errors = new BeanPropertyBindingResult(this.productComent, "");
		pcv.validate(this.productComent, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenTitleDescriptionRatingNull() {
		this.productComent.setTitle("");
		this.productComent.setDescription("");
		this.productComent.setRating(null);
		ProductComentValidador pcv = new ProductComentValidador();
		Errors errors = new BeanPropertyBindingResult(this.productComent, "");
		pcv.validate(this.productComent, errors);
		Assertions.assertThat(errors.getFieldError("rating").getCode())
				.isEqualTo("You must fill either the title and description or the rating");
	}

	@Test
	void shouldNotValidateWhenTitleEmptyDescriptionFull() {
		this.productComent.setTitle("");
		this.productComent.setDescription("Descripcion");
		ProductComentValidador pcv = new ProductComentValidador();
		Errors errors = new BeanPropertyBindingResult(this.productComent, "");
		pcv.validate(this.productComent, errors);
		Assertions.assertThat(errors.getFieldError("title").getCode()).isEqualTo("You must enter a title");
	}

	@Test
	void shouldNotValidateWhenTitleFullDescriptionEmpty() {
		this.productComent.setTitle("Titulo");
		this.productComent.setDescription("");
		ProductComentValidador pcv = new ProductComentValidador();
		Errors errors = new BeanPropertyBindingResult(this.productComent, "");
		pcv.validate(this.productComent, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode()).isEqualTo("You must enter a description");
	}

}
