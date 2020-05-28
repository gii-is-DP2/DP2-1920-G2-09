
package org.springframework.samples.petclinic.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.WalkValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class WalkValidatorTests {

	private Walk walk;

	@BeforeEach
	void setInitialWalk() {
		Walk w = new Walk();
		w.setName("Nombre");
		w.setDescription("Descripción de prueba");
		w.setMap("http://www.urldeprueba.com");
		this.walk = w;
	}

	// NoErrors
	@Test
	void shouldValidateWalkWithoutErrors() {
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	// Name
	@Test
	void shouldNotValidateWhenNameEmpty() {
		this.walk.setName("");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("The length of the name must be greater than 3 and no less than 50 characters");
	}

	@Test
	void shouldNotValidateNameIfLessThanLimitCharacters() {
		this.walk.setName("3j");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("The length of the name must be greater than 3 and no less than 50 characters");
	}

	@Test
	void shouldNotValidateNameIfLongerThanLimitCharacters() {
		this.walk.setName(
				"ESTA DESCRIPCIÓN ES DEMASIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADO"
						+ "LARGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("The length of the name must be greater than 3 and no less than 50 characters");
	}

	// Description
	@Test
	void shouldNotValidateWhenDescriptionEmpty() {
		this.walk.setDescription("");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("The length of the description must be greater than 3 and no less than 300 characters");
	}

	@Test
	void shouldNotValidateDescriptionIfLessThanLimitCharacters() {
		this.walk.setDescription("3j");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("The length of the description must be greater than 3 and no less than 300 characters");
	}

	@Test
	void shouldNotValidateDescriptionIfLongerThanLimitCharacters() {
		this.walk.setDescription(
				"ESTA DESCRIPCIÓN ES DEMASIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADO"
						+ "LARGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("The length of the description must be greater than 3 and no less than 300 characters");
	}

	// Map
	@Test
	void shouldNotValidateWhenMapEmpty() {
		this.walk.setMap("");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("map").getCode()).isEqualTo("The URL is not valid");
	}

	@Test
	void shouldNotValidateMapIfOffFormat() {
		this.walk.setMap("offFormat");
		WalkValidator wv = new WalkValidator();
		Errors errors = new BeanPropertyBindingResult(this.walk, "");
		wv.validate(this.walk, errors);
		Assertions.assertThat(errors.getFieldError("map").getCode()).isEqualTo("The URL is not valid");
	}
}
