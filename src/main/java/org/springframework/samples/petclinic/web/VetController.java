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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private static final String VIEWS_VETS_CREATE_OR_UPDATE_FORM = "vets/createOrUpdateVetForm";

	private final VetService vetService;

	@Autowired
	public VetController(final VetService clinicService) {
		this.vetService = clinicService;
	}

	@ModelAttribute("specialties")
	public Iterable<Specialty> populateSpecialties() {
		return this.vetService.findAllSpecialties();
	}

	@GetMapping(value = { "/vets" })
	public String showVetList(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll((Collection<? extends Vet>) this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = { "/vets.xml" })
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of
		// Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll((Collection<? extends Vet>) this.vetService.findVets());
		return vets;
	}

	@GetMapping(value = "/vets/new")
	public String initCreationForm(final ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/new")
	public String processCreationForm(final ModelMap model, @ModelAttribute("vet") final Vet vet,
			final BindingResult result, @RequestParam(required = false) final Integer[] specialties)
			throws DuplicatedUsernameException {
		VetValidator vetValidator = new VetValidator();
		Errors vetErrors = new BeanPropertyBindingResult(vet, "vet");
		vetValidator.validate(vet, vetErrors);

		if (vetErrors.hasErrors()) {
			result.addAllErrors(vetErrors);
			model.put("vet", vet);
			return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		} else {
			if (specialties != null) {
				Set<Specialty> esp = this.vetService.findSpecialtiesById(specialties);
				for (Specialty e : esp) {
					vet.addSpecialty(e);
				}
			}
			vet.getUser().setEnabled(true);
			try {
				this.vetService.saveVet(vet);
			} catch (DuplicatedUsernameException ex) {
				result.rejectValue("user.username", "duplicate", "already exists");
				return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
			}

			return "redirect:/vets/";
		}
	}

}
