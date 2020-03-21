package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.repository.SpecialtyRepository;

/**
 * Spring Data JPA specialization of the {@link SpecialtyRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataSpecialtyRepository extends CrudRepository<Specialty, Integer> {
	
	@Query("SELECT e from Specialty e where e.id = ?1 ")
	Specialty findOne(int idSpecialty);
	
}
