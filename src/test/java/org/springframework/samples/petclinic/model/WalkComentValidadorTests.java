
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.WalkComentValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class WalkComentValidadorTests {

	private WalkComent walkComent;

	@BeforeEach
	void setInitialWalkComent() {
		WalkComent wc = new WalkComent();
		Walk w = new Walk();
		w.setName("Producto de prueba");
		w.setDescription("Descripción de prueba");
		w.setMap("http://url.com");
		User u = new User();
		u.setEnabled(true);
		u.setPassword("contraseña");
		u.setUsername("u");
		wc.setDescription("Descripcion de prueba");

		wc.setPostDate(LocalDate.now().minusDays(2));
		wc.setWalk(w);
		wc.setRating(4);
		wc.setTitle("Esto es un titulo");
		wc.setUser(u);
		this.walkComent = wc;
	}

	@Test
	void shouldValidateWalktComentWithoutErrors() {
		WalkComentValidator wcv = new WalkComentValidator();
		Errors errors = new BeanPropertyBindingResult(this.walkComent, "");
		wcv.validate(this.walkComent, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenTitleDescriptionRatingNull() {
		this.walkComent.setTitle("");
		this.walkComent.setDescription("");
		this.walkComent.setRating(null);
		WalkComentValidator wcv = new WalkComentValidator();
		Errors errors = new BeanPropertyBindingResult(this.walkComent, "");
		wcv.validate(this.walkComent, errors);
		Assertions.assertThat(errors.getFieldError("rating").getCode())
				.isEqualTo("You must fill either the title and description or the rating");
	}

	@Test
	void shouldNotValidateWhenTitleEmptyDescriptionFull() {
		this.walkComent.setTitle("");
		this.walkComent.setDescription("Descripcion");
		WalkComentValidator wcv = new WalkComentValidator();
		Errors errors = new BeanPropertyBindingResult(this.walkComent, "");
		wcv.validate(this.walkComent, errors);
		Assertions.assertThat(errors.getFieldError("title").getCode()).isEqualTo("You must enter a title");
	}

	@Test
	void shouldNotValidateWhenTitleFullDescriptionEmpty() {
		this.walkComent.setTitle("Titulo");
		this.walkComent.setDescription("");
		WalkComentValidator wcv = new WalkComentValidator();
		Errors errors = new BeanPropertyBindingResult(this.walkComent, "");
		wcv.validate(this.walkComent, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode()).isEqualTo("You must enter a description");
	}

}
