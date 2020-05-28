package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.repository.WalkRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalkService {

	private WalkRepository walkRepository;

	@Autowired
	public WalkService(final WalkRepository walkRepository) {
		this.walkRepository = walkRepository;
	}

	@Transactional
	public Iterable<Walk> findAllWalks() {
		return this.walkRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Walk findWalkById(final int id) {
		return this.walkRepository.findById(id);
	}

	@Transactional
	public void saveWalk(final Walk walk) {
		this.walkRepository.save(walk);
	}

	@Transactional
	public void deleteWalk(final int id) {
		this.walkRepository.deleteById(id);
	}

}
