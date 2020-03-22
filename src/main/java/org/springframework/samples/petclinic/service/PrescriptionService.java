package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataPrescriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrescriptionService {

	private SpringDataPrescriptionRepository prescriptionRepository;
	
	@Autowired
	public PrescriptionService(SpringDataPrescriptionRepository prescriptionRepository ) {
		
		this.prescriptionRepository = prescriptionRepository;
	}
	
	@Transactional
	public void savePrescription(Prescription prescription) throws DataAccessException {
		prescriptionRepository.save(prescription);
	}

	@Transactional(readOnly = true)
	public Collection<Prescription> findPrescriptionsByPetId(int petId) {
		
		return prescriptionRepository.findPrescriptionByPetId(petId);
	}

	@Transactional(readOnly = true)
	public Prescription findPrescriptionById(int prId) {
		return prescriptionRepository.findById(prId).orElse(null);
		
	}

}
