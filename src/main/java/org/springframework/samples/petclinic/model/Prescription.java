package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "prescriptions")
public class Prescription extends NamedEntity {
	
	@Column(name = "description")  
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;


	@Column(name = "date")        
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate date;
	
	
	
	public LocalDate getDate() {
		return date;
	}


	public void setDate(LocalDate prescriptionDate) {
		this.date = prescriptionDate;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Pet getPet() {
		return pet;
	}


	public void setPet(Pet pet) {
		this.pet = pet;
	}


	public Vet getVet() {
		return vet;
	}


	public void setVet(Vet vet) {
		this.vet = vet;
	}
	
	
	
	
	

}
