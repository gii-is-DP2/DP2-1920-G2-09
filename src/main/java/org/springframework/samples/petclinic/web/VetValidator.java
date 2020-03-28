/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Vet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Vet</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to
 * define such validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class VetValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(final Object obj, final Errors errors) {
		Vet vet = (Vet) obj;

		// first name validation
		if (!StringUtils.hasLength(vet.getFirstName()) || vet.getFirstName().length() > 50
				|| vet.getFirstName().length() < 3) {
			errors.rejectValue("firstName", VetValidator.REQUIRED + " and between 3 and 50 characters",
					VetValidator.REQUIRED + " and between 3 and 50 character");
		}

		// last name validation
		if (!StringUtils.hasLength(vet.getLastName()) || vet.getLastName().length() > 50
				|| vet.getLastName().length() < 3) {
			errors.rejectValue("lastName", VetValidator.REQUIRED + " and between 3 and 50 characters",
					VetValidator.REQUIRED + " and between 3 and 50 character");
		}

		// username validation
		if (!StringUtils.hasLength(vet.getUser().getUsername()) || vet.getUser().getUsername().length() > 20
				|| vet.getUser().getUsername().length() < 3) {
			errors.rejectValue("user.username", VetValidator.REQUIRED + " and between 3 and 20 characters",
					VetValidator.REQUIRED + " and between 3 and 50 character");
		}

		// password validation
		if (!StringUtils.hasLength(vet.getUser().getPassword()) || vet.getUser().getUsername().length() > 50
				|| vet.getUser().getUsername().length() < 3) {
			errors.rejectValue("user.password", VetValidator.REQUIRED + " and between 3 and 50 characters",
					VetValidator.REQUIRED + " and between 3 and 50 character");
		}

		if (!StringUtils.hasLength(vet.getUser().getEmail()) || vet.getUser().getEmail().length() > 60
				|| vet.getUser().getEmail().length() < 10) {
			errors.rejectValue("user.email", VetValidator.REQUIRED + " and between 10 and 60 characters",
					VetValidator.REQUIRED + " and between 10 and 60 character");
		}

		if (!vet.getUser().getEmail().matches("^(.+)@(.+)$")) {
			errors.rejectValue("user.email", "The email is not correct", "The email is not correct");
		}
	}

	/**
	 * This Validator validates *just* Vet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Vet.class.isAssignableFrom(clazz);
	}

}
