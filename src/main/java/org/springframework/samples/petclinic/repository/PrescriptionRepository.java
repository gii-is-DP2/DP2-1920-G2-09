package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.samples.petclinic.model.Prescription;

public interface PrescriptionRepository {

	void save(Prescription prescription);

	List<Prescription> findByPetId(Integer petId);

	Collection<Prescription> findPrescriptionByPetId(int petId);

}
