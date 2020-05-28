
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.samples.petclinic.repository.WalkComentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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
	public WalkComent findWalkComentsById(final Integer walkComentId) {
		return this.walkComentRepository.findWalkComentById(walkComentId);
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
		if (this.walkComentRepository.getAverageRatingOfWalk(walkId) == null) {
			return 0.0;
		} else {
			return this.walkComentRepository.getAverageRatingOfWalk(walkId);
		}

	}

	@Transactional
	public void deleteWalkComent(final int comentId) {
		this.walkComentRepository.deleteComentById(comentId);
	}

}
