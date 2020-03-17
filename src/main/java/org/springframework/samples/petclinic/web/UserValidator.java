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

import org.springframework.samples.petclinic.model.User;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>User</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 */
public class UserValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		User user = (User) obj;
		
		// username validation
		if (!StringUtils.hasLength(user.getUsername()) || user.getUsername().length()>50 || user.getUsername().length()<3) {
			errors.rejectValue("username", REQUIRED+" and between 3 and 50 characters", REQUIRED+" and between 3 and 50 character");
		}

		// password validation
		if (!StringUtils.hasLength(user.getPassword())) {
			errors.rejectValue("password", REQUIRED+" and between 3 and 50 characters", REQUIRED+" and between 3 and 50 character");
		}
	}

	/**
	 * This Validator validates *just* User instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}

}
