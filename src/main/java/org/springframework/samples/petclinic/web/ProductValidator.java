package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Product;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ProductValidator implements Validator {

    @Override
    public boolean supports(final Class<?> clazz) {
	return Product.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
	Product p = (Product) target;

	if (p.getName() == null || p.getName() == "") {
	    errors.rejectValue("name", "The name of the object cannnot be empty",
		    "The name of the object cannnot be empty");

	}

	if (p.getDescription().length() < 20 || p.getDescription().length() > 128) {
	    errors.rejectValue("description", "The lenght of the description must be between 20 and 128 characters",
		    "The lenght of the description must be between 20 and 128 characters");
	}

	if (p.getStock() < 0) {
	    errors.rejectValue("stock", "The minimum stock possible is 0 units",
		    "The minimum stock possible is 0 units");
	}

	if (p.getUnitPrice() <= 0) {
	    errors.rejectValue("unitPrice", "The price of the product must be more than 0 euros",
		    "The price of the product must be more than 0 euros");

	}
	if (p.getCategory() == null) {
	    errors.rejectValue("category", "You must select one category", "You must select one category");
	}

    }

}
