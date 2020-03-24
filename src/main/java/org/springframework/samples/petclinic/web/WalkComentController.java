
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.samples.petclinic.service.WalkComentService;
import org.springframework.samples.petclinic.service.WalkService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WalkComentController {

	private final WalkComentService	walkComentService;
	private final WalkService		walkService;


	@Autowired
	public WalkComentController(final WalkComentService walkComentService, final WalkService walkService) {
		this.walkComentService = walkComentService;
		this.walkService = walkService;

	}

	@PostMapping("walks/{walkId}/add-walk-coment")
	public String saveWalkComent(@PathVariable("walkId") final int walkId, @Valid final WalkComent walkComent, final BindingResult result, final ModelMap model) {
		WalkComentValidator w = new WalkComentValidator();
		Errors errors = new BeanPropertyBindingResult(walkComent, "walkComent");
		w.validate(walkComent, errors);
		if (errors.hasErrors()) {
			model.put("walkComent", walkComent);
			result.addAllErrors(errors);
			return new WalkController(this.walkService, this.walkComentService).showWalk(walkId, model);
		} else {
			Walk walk = this.walkService.findWalkById(walkId);

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			Object principal = auth.getPrincipal();
			UserDetails us = null;
			if (principal instanceof UserDetails) {
				us = (UserDetails) principal;
			}

			User user = this.walkComentService.findUserByUsername(us.getUsername());
			walkComent.setWalk(walk);
			walkComent.setUser(user);
			walkComent.setPostDate(LocalDate.now());
			this.walkComentService.saveWalkComent(walkComent);
			model.addAttribute("OKmessage", "Your comment have been submited correctly");
			return new WalkController(this.walkService, this.walkComentService).showWalk(walkId, model);
		}

	}
}
