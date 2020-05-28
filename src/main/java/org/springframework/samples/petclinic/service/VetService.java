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
package org.springframework.samples.petclinic.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Authorities;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.AuthoritiesRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataSpecialtyRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataVetRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class VetService {

	private SpringDataVetRepository vetRepository;

	private SpringDataSpecialtyRepository specialtyRepository;

	private AuthoritiesRepository authoritiesRepository;

	@Autowired
	public VetService(final SpringDataVetRepository vetRepository,
			final SpringDataSpecialtyRepository specialtyRepository,
			final AuthoritiesRepository authoritiesRepository) {
		this.vetRepository = vetRepository;
		this.specialtyRepository = specialtyRepository;
		this.authoritiesRepository = authoritiesRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<Vet> findVets() {
		return this.vetRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Vet findVetById(final int id) {
		return this.vetRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Set<Specialty> findSpecialtiesById(final Integer[] ids) {
		Set<Specialty> res = new HashSet<>();
		for (Integer id : ids) {
			res.add(this.specialtyRepository.findOne(id));
		}
		return res;
	}

	@Transactional
	public Iterable<Specialty> findAllSpecialties() {
		return this.specialtyRepository.findAll();
	}

	@Transactional
	public void saveVet(final Vet vet) throws DuplicatedUsernameException {
		Integer countUsersWithSameUsername = this.vetRepository
				.countOwnersWithSameUserName(vet.getUser().getUsername());
		if (countUsersWithSameUsername > 0) {
			throw new DuplicatedUsernameException();
		}
		Authorities author = new Authorities();
		author.setAuthority("veterinarian");
		author.setUsername(vet.getUser().getUsername());
		this.authoritiesRepository.save(author);
		this.vetRepository.save(vet);
	}

	@Transactional(readOnly = true)
	public Vet findVetbyUser(final String User) {
		return this.vetRepository.findVetbyUser(User);
	}

}
