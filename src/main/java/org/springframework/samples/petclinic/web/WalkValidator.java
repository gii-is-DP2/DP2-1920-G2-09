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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.samples.petclinic.model.Walk;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * <code>Validator</code> for <code>Walk</code> forms.
 * <p>
 * We're not using Bean Validation annotations here because it is easier to define such
 * validation rule in Java.
 * </p>
 *
 */
public class WalkValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Walk.class.isAssignableFrom(clazz);
	}
	
	private static final String URL_REGEX =
			"^((((https?|ftps?|gopher|telnet|nntp)://))" +
			"(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
			"([).!';/?:,][[:blank:]])?$";

	private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);

	public static boolean urlValidator(String url) {

		if (url == null) {
			return false;
		}

		Matcher matcher = URL_PATTERN.matcher(url);
		return matcher.matches();
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		Walk walk = (Walk) obj;
		
		String name = walk.getName();
		String description = walk.getDescription();
		String map = walk.getMap();
		
		// name validation
		if (!StringUtils.hasLength(name) || name.length()>50 || name.length()<3) {
			errors.rejectValue("name", "The length of the name must be greater than 3 and no less than 50 characters",
					"The length of the name must be greater than 3 and no less than 50 characters");
		}
		
		// description validation
		if (!StringUtils.hasLength(description) || description.length()>300 || description.length()<3) {
			errors.rejectValue("description", "The length of the description must be greater than 3 and no less than 300 characters",
					"The length of the description must be greater than 3 and no less than 300 characters");
		}
		
		// map validation
		if (!StringUtils.hasLength(map) || urlValidator(map) == false) {
			errors.rejectValue("map", "The URL is not valid", "The URL is not valid");
		}
	}

}
