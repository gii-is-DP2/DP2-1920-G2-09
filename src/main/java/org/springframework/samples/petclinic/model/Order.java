package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "owner.id")
	private Owner owner;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate orderDate;

	@NotNull
	private Double totalPrice;
}
