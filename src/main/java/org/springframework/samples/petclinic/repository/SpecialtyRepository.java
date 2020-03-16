package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Specialty;

/**
 * Repository class for <code>Specialty</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface SpecialtyRepository {

	/**
	 * Retrieve all <code>Specialty</code>s from the data store.
	 * 
	 * @return a <code>Collection</code> of <code>Specialty</code>s
	 */
	Collection<Specialty> findAll() throws DataAccessException;

	/**
	 * Save a <code>Specialty</code> to the data store, either inserting or updating it.
	 * 
	 * @param specialty the <code>Specialty</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Specialty specialty) throws DataAccessException;
	
	Specialty findOne(int idSpecialty);

}
