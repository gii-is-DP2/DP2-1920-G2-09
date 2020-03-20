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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
	public Iterable<Walk> findAllWalks(){
		Iterable<Walk> walks = this.walkRepository.findAll();
		return walks;
	}
	
	@Transactional
	public Iterable<Walk> findAllWalksAvailable(){
		Iterable<Walk> walks = this.walkRepository.findAllAvailable();
		return walks;
	}
	
	@Transactional(readOnly = true)
	public Walk findWalkById(final int id) throws DataAccessException {
		return this.walkRepository.findById(id);
	}
	
	@Transactional
	public void saveWalk(final Walk walk){
		this.walkRepository.save(walk);
	}


}
