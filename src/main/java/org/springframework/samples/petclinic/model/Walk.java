
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
@Entity
@Table(name = "walks")
public class Walk extends NamedEntity {

	@NotEmpty
	private String		description;
	@NotEmpty
	@URL
	private String		map;
	@NotNull
	private boolean		available;

}
