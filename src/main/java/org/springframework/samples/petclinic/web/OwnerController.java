/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class OwnerController {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	private final OwnerService ownerService;
	private final PrescriptionService prescriptionService;
	private final String OWNER = "owner";
	private final String PAYMENT_DETAILS_PATH = "/owners/paymentDetails";

	@Autowired
	public OwnerController(final OwnerService ownerService, final UserService userService,
			final AuthoritiesService authoritiesService, final PrescriptionService prescriptionService) {
		this.ownerService = ownerService;
		this.prescriptionService = prescriptionService;
	}

	@ModelAttribute("months")
	public List<Integer> setMonths() {
		Integer[] a = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
		return Arrays.asList(a);
	}

	@ModelAttribute("years")
	public List<Integer> setYears() {
		List<Integer> ls = new ArrayList<>();
		Integer i = LocalDate.now().getYear();
		Integer fin = LocalDate.now().getYear() + 20;
		while (i < fin) {
			ls.add(i);
			i++;
		}
		return ls;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/owners/new")
	public String initCreationForm(final Map<String, Object> model) {
		Owner owner = new Owner();
		model.put(this.OWNER, owner);
		return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/new")
	public String processCreationForm(@Valid final Owner owner, final BindingResult result)
			throws DuplicatedUsernameException {
		if (result.hasErrors()) {
			return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			try {
				this.ownerService.saveOwner(owner);
			} catch (DuplicatedUsernameException ex) {
				result.rejectValue("user.username", "duplicate", "already exists");
				return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
			}
			// creating owner, user and authorities

			return "redirect:/owners/" + owner.getId();
		}
	}

	@GetMapping(value = "/owners/find")
	public String initFindForm(final Map<String, Object> model) {
		model.put(this.OWNER, new Owner());
		return "owners/findOwners";
	}

	@GetMapping(value = "/owners")
	public String processFindForm(Owner owner, final BindingResult result, final Map<String, Object> model) {

		// allow parameterless GET request for /owners to return all records
		if (owner.getLastName() == null) {
			owner.setLastName(""); // empty string signifies broadest possible search
		}

		// find owners by last name
		Collection<Owner> results = this.ownerService.findOwnerByLastName(owner.getLastName());
		if (results.isEmpty()) {
			// no owners found
			result.rejectValue("lastName", "notFound", "not found");
			return "owners/findOwners";
		} else if (results.size() == 1) {
			// 1 owner found
			owner = results.iterator().next();
			return "redirect:/owners/" + owner.getId();
		} else {
			// multiple owners found
			model.put("selections", results);
			return "owners/ownersList";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/edit")
	public String initUpdateOwnerForm(@PathVariable("ownerId") final int ownerId, final Model model) {
		Owner owner = this.ownerService.findOwnerById(ownerId);
		model.addAttribute(owner);
		return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/owners/{ownerId}/edit")
	public String processUpdateOwnerForm(@Valid final Owner owner, final BindingResult result,
			@PathVariable("ownerId") final int ownerId) {
		if (result.hasErrors()) {
			return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
		} else {
			try {
				owner.setId(ownerId);
				this.ownerService.saveOwner(owner);
			} catch (DuplicatedUsernameException ex) {
				result.rejectValue("user.username", "duplicate", "already exists");
				return OwnerController.VIEWS_OWNER_CREATE_OR_UPDATE_FORM;
			}

			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/payment-details")
	public String initPaymentDetailsForm(final Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		Owner owner = this.ownerService.findOwnerByUsername(us.getUsername());
		model.addAttribute(owner);
		return this.PAYMENT_DETAILS_PATH;
	}

	@PostMapping(value = "/owners/payment-details")
	public String processUpdateForm(final ModelMap model, @Valid final Owner own, final BindingResult result)
			throws DuplicatedUsernameException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		Owner owner = this.ownerService.findOwnerByUsername(us.getUsername());
		BeanUtils.copyProperties(own, owner, "id", "firstName", "lastName", "address", "city", "telephone", "pets",
				"user");
		PaymentDetailsValidator p = new PaymentDetailsValidator();
		Errors errors = new BeanPropertyBindingResult(owner, this.OWNER);
		p.validate(owner, errors);
		if (errors.hasErrors()) {
			result.addAllErrors(errors);
			model.put(this.OWNER, own);
			return this.PAYMENT_DETAILS_PATH;
		} else {
			this.ownerService.saveOwner(owner);
			model.addAttribute("OKmessage", "Your payment details have been saved");

			return this.PAYMENT_DETAILS_PATH;
		}
	}

	@GetMapping(value = "/owners/profile")
	public String showOwnerProfile(final ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		UserDetails us = null;
		if (principal instanceof UserDetails) {
			us = (UserDetails) principal;
		}
		Owner owner = this.ownerService.findOwnerByUsername(us.getUsername());
		Collection<Prescription> pcs = this.prescriptionService.findPrescriptionsByOwnerId(owner.getId());
		model.addAttribute("prescriptions", pcs);
		model.addAttribute(this.OWNER, owner);
		return "/owners/ownerProfile";
	}

	/**
	 * Custom handler for displaying an owner.
	 *
	 * @param ownerId the ID of the owner to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/owners/{ownerId}")
	public ModelAndView showOwner(@PathVariable("ownerId") final int ownerId) {
		ModelAndView mav = new ModelAndView("owners/ownerDetails");
		mav.addObject(this.ownerService.findOwnerById(ownerId));
		return mav;
	}

}
