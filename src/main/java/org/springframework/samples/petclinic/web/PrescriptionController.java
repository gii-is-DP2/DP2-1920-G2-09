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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class PrescriptionController {

	private final PetService petService;
	private final PrescriptionService prescriptionService;
	private final VetService vetService;

	@Autowired
public PrescriptionController(PetService petService, PrescriptionService prescriptionService,VetService vetService) {
		
		this.petService = petService;
		this.prescriptionService = prescriptionService;
		this.vetService = vetService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder("prescription")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new PrescriptionValidator());
	}
	


	@ModelAttribute("prescription")
	public Prescription loadPetWithPrescription(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Prescription prescription = new Prescription();
		pet.addPrescription(prescription);
		return prescription;
	}

	
	@GetMapping(value = "/owners/*/pets/{petId}/prescriptions/new")
	public String initNewPrescriptionForm(@PathVariable("petId") int petId,Map<String, Object> model) {
		
		
		
		model.put("previa", this.prescriptionService.findPrescriptionsByPetId(petId));
		
		
		return "prescriptions/createOrUpdatePrescriptionForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/prescriptions/new")
	public String processNewPrescriptionForm(@Valid Prescription prescription, BindingResult result) {
		if (result.hasErrors()) {
			return "prescriptions/createOrUpdatePrescriptionForm";
		}
		else {
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object sesion = auth.getPrincipal();
			UserDetails us = null;
			if(sesion instanceof UserDetails) {
				us = (UserDetails) sesion;
			}
			
			String userName = us.getUsername();
			Vet vet = this.vetService.findVetbyUser(userName);
			
			prescription.setVet(vet);
			
			
			this.prescriptionService.savePrescription(prescription);
			return "redirect:/owners/{ownerId}";
		}
	}


	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/prescriptions/list")
	public String listPrescriptions(@PathVariable("petId") int petId,Model model) {
		
		model.addAttribute("selections", this.prescriptionService.findPrescriptionsByPetId(petId));
		
		return "prescriptions/prescriptionsList";
	}
	
	
	@GetMapping("/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}")
	public String showPrescription(@PathVariable("prescriptionId") int prId,Map<String, Object> model) {
		
		Prescription pres =  this.prescriptionService.findPrescriptionById(prId).get();
		
		model.put("prescription",pres);
		
		return "prescriptions/prescriptionDetails";
	}
	}


