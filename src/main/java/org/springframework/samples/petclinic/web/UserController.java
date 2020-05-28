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

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.ShoppingCartService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class UserController {

	private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";

	private final OwnerService ownerService;
	private final ShoppingCartService shoppingCartService;

	@Autowired
	public UserController(final OwnerService clinicService, final ShoppingCartService shoppingCartService) {
		this.ownerService = clinicService;
		this.shoppingCartService = shoppingCartService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/users/new")
	public String initCreationForm(final Map<String, Object> model) {
		Owner owner = new Owner();
		User user = new User();
		owner.setUser(user);
		model.put("owner", owner);
		model.put("user", user);
		return UserController.VIEWS_OWNER_CREATE_FORM;
	}

	@PostMapping(value = "/users/new")
	public String processCreationForm(@Valid final Owner owner, final BindingResult result)
			throws DuplicatedUsernameException {
		if (result.hasErrors()) {
			return UserController.VIEWS_OWNER_CREATE_FORM;
		} else {

			try {
				ShoppingCart sp = new ShoppingCart();
				sp.setOwner(owner);
				this.ownerService.saveOwner(owner);
				this.shoppingCartService.saveShoppingCart(sp);
			} catch (DuplicatedUsernameException ex) {
				result.rejectValue("user.username", "duplicate", "already exists");
				return UserController.VIEWS_OWNER_CREATE_FORM;
			}
			// creating owner, user, and authority

			return "redirect:/";
		}
	}

}
