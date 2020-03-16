package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "prescriptions")
public class Prescription extends NamedEntity {
	
	@NotEmpty
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;

	@NotNull     
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate dateInicio;
	
	@NotNull  
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate dateFinal;
	

	public LocalDate getDateFinal() {
		return dateFinal;
	}


	public void setDateFinal(LocalDate dateFinal) {
		this.dateFinal = dateFinal;
	}


	public LocalDate getDateInicio() {
		return dateInicio;
	}


	public void setDateInicio(LocalDate prescriptionDate) {
		this.dateInicio = prescriptionDate;
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
