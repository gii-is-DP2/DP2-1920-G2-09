
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Entity
@Data
public class Product extends NamedEntity {

	@NotEmpty
	private String		description;
	@NotEmpty
	@URL
	private String		urlImage;
	@NotNull
	private int			stock;
	@NotNull
	private double		unitPrice;
	@NotNull
	private Category	category;
	@NotNull
	private boolean		available;

}
