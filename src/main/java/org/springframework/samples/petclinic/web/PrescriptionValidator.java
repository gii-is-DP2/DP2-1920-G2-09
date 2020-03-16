package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PrescriptionValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Prescription prescription = (Prescription) obj;
		String name = prescription.getName();
		// title validation
		if (!StringUtils.hasLength(name) || name.length()>20 || name.length()<3) {
			errors.rejectValue("name", REQUIRED+" and between 3 and 20 characters", REQUIRED+" and between 3 and 20 character");
		}


		// birth date validation
		if (prescription.getDateFinal() == null || prescription.getDateInicio()== null || 
				prescription.getDateInicio().isBefore(LocalDate.now())) {
			errors.rejectValue("dateInicio", "The Start Date can not be before today", "The Start Date can not be before today");
		}
		
		if (prescription.getDateFinal() == null || prescription.getDateInicio()== null || 
				 prescription.getDateInicio().isAfter(prescription.getDateFinal())) {
			errors.rejectValue("dateFinal", "The End Date can not be before Start Date", "The End Date can not be before Start Date");
		}
	
	
	    // description validation
	String description = prescription.getDescription();
	if (!StringUtils.hasLength(description) || description.length()>232 || description.length()<10) {
		errors.rejectValue("description", REQUIRED+" and between 10 and 232 characters", REQUIRED+" and between 10 and 232 character");
	}
	}
	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Prescription.class.isAssignableFrom(clazz);

}
}
