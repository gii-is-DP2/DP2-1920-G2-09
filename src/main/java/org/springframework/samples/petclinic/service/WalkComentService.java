
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.samples.petclinic.repository.WalkComentRepository;
import org.springframework.transaction.annotation.Transactional;

public class WalkComentService {

	WalkComentRepository walkComentRepository;


	@Autowired
	public WalkComentService(final WalkComentRepository walkComentRepository) {
		this.walkComentRepository = walkComentRepository;
	}

	@Transactional
	public Collection<WalkComent> findAllComentsOfTheWalk(final Integer idWalk) {
		return this.walkComentRepository.findComentsOfTheWalk(idWalk);
	}

	@Transactional
	public User findUserByUsername(final String username) {
		return this.walkComentRepository.findUserByUsername(username);
	}

	@Transactional
	public void saveWalkComent(final WalkComent wc) {
		this.walkComentRepository.save(wc);
	}

	@Transactional
	public Double getAverageRatingOfWalk(final int walkId) {
		return this.walkComentRepository.getAverageRatingOfWalk(walkId);
	}

}
