
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Product extends NamedEntity {

	@NotEmpty
	private String description;
	@NotEmpty
	private String urlImage;
	@NotNull
	private int stock;
	@NotNull
	private double unitPrice;
	@NotNull
	private Category category;
	@NotNull
	private boolean available;

}
