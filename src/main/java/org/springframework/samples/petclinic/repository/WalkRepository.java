package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Walk;

public interface WalkRepository extends CrudRepository<Walk, Integer> {

	Walk findById(int id);

	void deleteById(int id);

}
