
package org.springframework.samples.petclinic.model;

import java.util.HashSet;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.PaymentDetailsValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

public class PaymentDetailsValidatorTests {

	private Owner owner;


	@BeforeEach
	void setInitialOwner() {
		Owner o = new Owner();
		User u = new User();
		Set<Pet> pets = new HashSet<>();
		o.setUser(u);
		o.setAddress("C/ Virgen Del Rocio");
		o.setCity("Sevilla");
		o.setCreditCardNumber("1111222233334444");
		o.setCvv("123");
		o.setExpirationMonth(8);
		o.setExpirationYear(2022);
		o.setFirstName("Paco");
		o.setId(200);
		o.setLastName("Arroyo");
		o.setTelephone("620652666");
		o.setPetsInternal(pets);
		this.owner = o;
	}

	@Test
	void shouldValidateOwnerWithoutErrors() {
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenMissingCardNumber() {
		this.owner.setCreditCardNumber("");
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("You must fill all the fields");
	}

	@Test
	void shouldNotValidateWhenMissingCvv() {
		this.owner.setCvv(null);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("You must fill all the fields");
	}

	@Test
	void shouldNotValidateWhenMissingExpirationMonth() {
		this.owner.setExpirationMonth(null);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("You must fill all the fields");
	}

	@Test
	void shouldNotValidateWhenMissingExpirationYear() {
		this.owner.setExpirationYear(null);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("You must fill all the fields");
	}

	@Test
	void shouldNotValidateWhenCardNumberWrong() {
		this.owner.setCreditCardNumber("1111");
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("creditCardNumber").getCode()).isEqualTo("Credit Card Number is not correct");
	}

	@Test
	void shouldNotValidateWhenCvvHasLetters() {
		this.owner.setCvv("ABC");
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("cvv").getCode()).isEqualTo("CVV must only contains numbers");
	}

	@Test
	void shouldNotValidateWhenCvvHasLessThanThreeNumbers() {
		this.owner.setCvv("12");
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("cvv").getCode()).isEqualTo("CVV must have 3 numbers");
	}

	@Test
	void shouldNotValidateWhenExpirationMonthLessThanOne() {
		this.owner.setExpirationMonth(0);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationMonth").getCode()).isEqualTo("The Month must be between 1 and 12");
	}

	@Test
	void shouldNotValidateWhenExpirationYearIsNegative() {
		this.owner.setExpirationYear(-190);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("The Year must be positive");
	}

	@Test
	void shouldNotValidateWhenDateIsPassed() {
		this.owner.setExpirationYear(2020);
		this.owner.setExpirationMonth(1);
		PaymentDetailsValidator ov = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(this.owner, "");
		ov.validate(this.owner, errors);
		Assertions.assertThat(errors.getFieldError("expirationYear").getCode()).isEqualTo("The Credit Card you introduced has expired");
	}

}
