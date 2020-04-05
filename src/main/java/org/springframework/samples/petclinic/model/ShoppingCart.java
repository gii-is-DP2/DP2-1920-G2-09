package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.Valid;

import lombok.Data;

@Data
@Entity
public class ShoppingCart extends BaseEntity {

	@Valid
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "owner.id")
	private Owner owner;

}
