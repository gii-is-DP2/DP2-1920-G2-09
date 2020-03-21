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

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.samples.petclinic.service.WalkComentService;
import org.springframework.samples.petclinic.service.WalkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/walks")
public class WalkController {

	private final WalkService		walkService;
	private final WalkComentService	walkComentService;


	@Autowired
	public WalkController(final WalkService walkService, final WalkComentService walkComentService) {
		this.walkService = walkService;
		this.walkComentService = walkComentService;
	}

	@InitBinder("walk")
	public void initWalkBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new WalkValidator());
	}

	@GetMapping(value = "/all")
	public String findAllWalks(final ModelMap model) {
		Walk w = new Walk();
		model.put("walk", w);
		Iterable<Walk> walks = this.walkService.findAllWalks();
		model.addAttribute("walks", walks);
		return "walks/listWalks";
	}

	@GetMapping(value = "/userAll")
	public String findAllWalksAvailable(final ModelMap model) {
		Walk w = new Walk();
		model.put("walk", w);
		Iterable<Walk> walks = this.walkService.findAllWalksAvailable();
		model.addAttribute("walks", walks);
		return "walks/listWalks";
	}

	@GetMapping(value = "/{walkId}")
	public String showWalk(@PathVariable("walkId") final int walkId, final ModelMap model) {
		WalkComent wc = new WalkComent();
		Collection<WalkComent> coments = this.walkComentService.findAllComentsOfTheWalk(walkId);
		if (!model.containsAttribute("OKmessage") && model.containsAttribute("walkComent")) {
			model.put("walkComent", model.get("walkComent"));
		} else {
			model.put("walkComent", wc);
		}
		Double rating = this.walkComentService.getAverageRatingOfWalk(walkId);
		model.put("rating", rating == null ? 0.0 : rating);
		model.put("coments", coments);
		model.put("walk", this.walkService.findWalkById(walkId));
		return "walks/walkDetails";
	}

	@GetMapping(value = "/new")
	public String initCreationForm(final ModelMap model) {
		Walk walk = new Walk();
		model.put("walk", walk);
		return "walks/createOrUpdateWalkForm";
	}

	@PostMapping(value = "/new")
	public String processCreationForm(@Valid final Walk walk, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("walk", walk);
			return "walks/createOrUpdateWalkForm";
		} else {
			this.walkService.saveWalk(walk);
			return this.findAllWalks(model);
		}
	}

}
