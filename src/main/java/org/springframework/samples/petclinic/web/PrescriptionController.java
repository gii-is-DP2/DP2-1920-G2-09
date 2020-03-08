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
import java.util.Optional;
import java.util.stream.Collector;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class PrescriptionController {

	private final PetService petService;

	@Autowired
	public PrescriptionController(PetService petService) {
		this.petService = petService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
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
		
		
		model.put("previa", this.petService.findPrescriptionsByPetId(petId));
		
		return "prescriptions/createOrUpdatePrescriptionForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/prescriptions/new")
	public String processNewPrescriptionForm(@Valid Prescription prescription, BindingResult result) {
		if (result.hasErrors()) {
			return "prescriptions/createOrUpdatePrescriptionForm";
		}
		else {
			this.petService.savePrescription(prescription);
			return "redirect:/owners/{ownerId}";
		}
	}

//	@GetMapping(value = "/owners/*/pets/{petId}/prescriptions")
//	public String showPrescriptions(@PathVariable int petId, Map<String, Object> model) {
//		model.put("prescriptions", this.petService.findPetById(petId).getPrescriptions());
//		return "prescriptionsList";
//	}
	
//	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/prescriptions/list")
//	public String processFindForm(@PathVariable("petId") int petId, BindingResult result, Map<String, Object> model) {
//
//		
//		Collection<Prescription> results = this.petService.findPrescriptionsByPetId(petId);
//		
//			model.put("selections", results);
//			return "prescriptions/prescriptionsList";
//		}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/prescriptions/list")
	public String listPrescriptions(@PathVariable("petId") int petId,Model model) {
		
		model.addAttribute("selections", this.petService.findPrescriptionsByPetId(petId));
		
		return "prescriptions/prescriptionsList";
	}
	
	
	@GetMapping("/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}")
	public String showPrescription(@PathVariable("prescriptionId") int prId,Map<String, Object> model) {
		
		Prescription pres =  this.petService.findPrescriptionById(prId).get();
		
		model.put("prescription",pres);
		
		return "prescriptions/prescriptionDetails";
	}
	}


