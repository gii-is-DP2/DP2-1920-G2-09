package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
public class Item extends BaseEntity {

	@Valid
	@ManyToOne(cascade = CascadeType.PERSIST)
	private ShoppingCart shoppingCart;

	@ManyToOne(cascade = CascadeType.PERSIST)
	private Order order;

	@Valid
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Product product;

	@NotNull
	private Integer quantity;

	@NotNull
	private Double unitPrice;

}
