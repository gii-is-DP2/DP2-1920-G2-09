package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductComentValidador implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
	return ProductComent.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
	ProductComent pc = (ProductComent) target;
	String title = pc.getTitle();
	String description = pc.getDescription();

	if (title == "" && description == "" && pc.getRating() == null) {
	    errors.rejectValue("rating", "You must fill either the title and description or the rating",
		    "You must fill either the title and description or the rating");
	}

	if (title.equals("") && !description.equals("")) {
	    errors.rejectValue("title", "You must enter a title", "You must enter a title");
	}

	if (!title.equals("") && description.equals("")) {
	    errors.rejectValue("description", "You must enter a description", "You must enter a description");
	}

    }

}
