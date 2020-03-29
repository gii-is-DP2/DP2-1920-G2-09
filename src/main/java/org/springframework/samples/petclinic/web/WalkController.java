package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
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

	private final WalkService walkService;
	private final WalkComentService walkComentService;

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
		model.put("rating", rating);
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

	@GetMapping(value = "/{walkId}/edit")
	public String initUpdateWalkForm(@PathVariable("walkId") final int walkId, final ModelMap model) {
		Walk walk = this.walkService.findWalkById(walkId);
		model.put("walk", walk);
		return "walks/createOrUpdateWalkForm";
	}

	@PostMapping(value = "/{walkId}/edit")
	public String processUpdateWalkForm(@Valid final Walk walk, final BindingResult result,
			@PathVariable("walkId") final int walkId, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("walk", walk);
			return "walks/createOrUpdateWalkForm";
		} else {
			Walk walkToUpdate = this.walkService.findWalkById(walkId);
			BeanUtils.copyProperties(walk, walkToUpdate, "id");
			this.walkService.saveWalk(walkToUpdate);
			return "redirect:/walks/{walkId}";
		}
	}

	@GetMapping(value = "/{walkId}/delete")
	public String initDeleteWalk(@PathVariable("walkId") final int walkId, final ModelMap model) {
		Collection<WalkComent> walkComent = this.walkComentService.findAllComentsOfTheWalk(walkId);
		
		
		if(walkComent.isEmpty()) {
			this.walkService.deleteWalk(walkId);
			
		}else {
			for(WalkComent w: walkComent) {
				this.walkComentService.deleteWalkComent(w.getId());
			}
			this.walkService.deleteWalk(walkId);
		}
		
		return this.findAllWalks(model);
	}

}
