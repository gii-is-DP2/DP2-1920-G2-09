
package org.springframework.samples.petclinic.model;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.VetValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class VetValidatorTests {

	private Vet vet;

	@BeforeEach
	void setInitialVet() {
		Vet v = new Vet();
		v.setFirstName("Javier");
		v.setLastName("Arroyo Blanco");
		Set<Specialty> specialties = new HashSet<Specialty>();
		v.setSpecialtiesInternal(specialties);
		User u = new User();
		u.setUsername("javarrbla");
		u.setEmail("emailBienHecho@gmail.com");
		u.setPassword("123456");
		u.setEnabled(true);
		v.setUser(u);
		this.vet = v;
	}

	@Test
	void shouldValidateVetWithoutErrors() {
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenFirstNameEmpty() {
		this.vet.setFirstName("");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("firstName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateFirstNameIsLessThan3Characters() {
		this.vet.setFirstName("-3");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("firstName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateFirstNameIsLongerThan50Characters() {
		this.vet.setFirstName(
				"Demasiadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos caracteres");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("firstName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateWhenLastNameEmpty() {
		this.vet.setLastName("");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("lastName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateLastNameIsLessThan3Characters() {
		this.vet.setLastName("-3");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("lastName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateLastNameIsLongerThan50Characters() {
		this.vet.setLastName(
				"Demasiadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos caracteres");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("lastName").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateWhenUsernameEmpty() {
		this.vet.getUser().setUsername("");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.username").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateUsernameIsLessThan3Characters() {
		this.vet.getUser().setUsername("-3");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.username").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateUsernameIsLongerThan20Characters() {
		this.vet.getUser().setUsername("Demasiadoooooooooooooooooooooooos caracteres");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.username").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateWhenPasswordEmpty() {
		this.vet.getUser().setPassword("");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.password").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidatePasswordIsLessThan3Characters() {
		this.vet.getUser().setUsername("-3");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.password").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidatePasswordIsLongerThan50Characters() {
		this.vet.getUser().setUsername(
				"Demasiadoooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooos caracteres");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.password").getCode())
				.isEqualTo("required and between 3 and 50 characters");
	}

	@Test
	void shouldNotValidateEmailIsLessThan10Characters() {
		this.vet.getUser().setEmail("a@g.com");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.email").getCode())
				.isEqualTo("required and between 10 and 60 characters");
	}

	@Test
	void shouldNotValidateEmailIsLongerThan60Characters() {
		this.vet.getUser().setEmail(
				"eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeddddddddddddddddddddddddddddddddddddddddeeeemaasdddddddddddddddddddddddd@muylargo.com");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.email").getCode())
				.isEqualTo("required and between 10 and 60 characters");
	}

	@Test
	void shouldNotValidateEmailIsNotCorrect() {
		this.vet.getUser().setEmail("emailMalFormado#####c.com");
		VetValidator vv = new VetValidator();
		Errors errors = new BeanPropertyBindingResult(this.vet, "");
		vv.validate(this.vet, errors);
		Assertions.assertThat(errors.getFieldError("user.email").getCode()).isEqualTo("The email is not correct");
	}
}
