package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Prescription;

public interface PrescriptionRepository {
	
	void save(Prescription prescription) throws DataAccessException;

	List<Prescription> findByPetId(Integer petId);

}
