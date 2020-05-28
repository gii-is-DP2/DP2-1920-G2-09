package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.web.PrescriptionValidator;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

class ValidatorPrescriptionTests {

	private Prescription prescription;
	private Pet pet;
	private Vet vet;

	@BeforeEach
	void setInitialPrescription() {
		Prescription p = new Prescription();
		Pet pet = new Pet();
		Vet vet = new Vet();
		p.setName("Titulo de Pueba");
		p.setDateInicio(LocalDate.of(2020, 12, 12));
		p.setDateFinal(LocalDate.of(2020, 12, 15));
		p.setDescription("esta es una descripcion de prueba para los test");
		p.setPet(pet);
		p.setVet(vet);
		this.prescription = p;
	}

	@Test
	void shouldValidatePrescriptionWithoutErrors() {

		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getErrorCount()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenTitleEmpty() {
		this.prescription.setName("");
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateTitleIsLessThan3Characters() {
		this.prescription.setName("ja");
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateTitleIsLongerThan20Characters() {
		this.prescription.setName("hkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkñokjkkkkkkkkkkkkkkkkkkkkkkkkkk");
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("name").getCode())
				.isEqualTo("required and between 3 and 20 characters");
	}

	@Test
	void shouldNotValidateDescriptionIsLessThan10Characters() {
		this.prescription.setDescription("menos10");
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("required and between 10 and 232 characters");
	}

	@Test
	void shouldNotValidateDescriptionIsMoreThan232Characters() {
		this.prescription.setDescription(
				"maddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("description").getCode())
				.isEqualTo("required and between 10 and 232 characters");
	}

	@Test
	void shouldNotValidateStartDayBeforeToday() {
		this.prescription.setDateInicio(LocalDate.of(2010, 10, 10));
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("dateInicio").getCode())
				.isEqualTo("The Start Date can not be before today");
	}

	@Test
	void shouldNotValidateEndDayBeforeStartDay() {
		this.prescription.setDateFinal(LocalDate.of(2000, 10, 10));
		this.prescription.setDateInicio(LocalDate.of(2010, 10, 10));
		PrescriptionValidator pv = new PrescriptionValidator();
		Errors errors = new BeanPropertyBindingResult(this.prescription, "");
		pv.validate(this.prescription, errors);
		Assertions.assertThat(errors.getFieldError("dateFinal").getCode())
				.isEqualTo("The End Date can not be before Start Date");
	}

}
