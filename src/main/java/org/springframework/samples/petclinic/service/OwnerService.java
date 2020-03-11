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

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.springdatajpa.OwnerCrudRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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
public class OwnerService {

    private OwnerRepository ownerRepository;
    private OwnerCrudRepository ownerCrudRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthoritiesService authoritiesService;

    @Autowired
    public OwnerService(final OwnerRepository ownerRepository, final OwnerCrudRepository ownerCrudRepository) {
	this.ownerRepository = ownerRepository;
	this.ownerCrudRepository = ownerCrudRepository;
    }

    @Transactional(readOnly = true)
    public Owner findOwnerById(final int id) throws DataAccessException {
	return this.ownerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByLastName(final String lastName) throws DataAccessException {
	return this.ownerRepository.findByLastName(lastName);
    }

    @Transactional(rollbackFor = DuplicatedPetNameException.class)
    public void saveOwner(final Owner owner) throws DuplicatedUsernameException {
	Integer countOwnerWithSameUsername = this.ownerCrudRepository
		.countOwnersWithSameUserName(owner.getUser().getUsername());
	// CASO DE CREAR

	if (owner.getId() == null && countOwnerWithSameUsername > 0) {
	    throw new DuplicatedUsernameException();
	    // CASO EDITAR
	} else if (owner.getId() != null) {
	    Owner old = this.ownerCrudRepository.findById(owner.getId()).get();
	    if (!owner.getUser().getUsername().equals(old.getUser().getUsername()) && countOwnerWithSameUsername > 0) {
		throw new DuplicatedUsernameException();
	    } else {
		this.ownerRepository.save(owner);
		// creating user
		this.userService.saveUser(owner.getUser());
		// creating authorities
		this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	    }
	} else {
	    // creating owner
	    this.ownerRepository.save(owner);
	    // creating user
	    this.userService.saveUser(owner.getUser());
	    // creating authorities
	    this.authoritiesService.saveAuthorities(owner.getUser().getUsername(), "owner");
	}

    }

    @Transactional
    public Owner findOwnerByUsername(final String username) {
	return this.ownerCrudRepository.findOwnerByUsername(username);
    }

}
