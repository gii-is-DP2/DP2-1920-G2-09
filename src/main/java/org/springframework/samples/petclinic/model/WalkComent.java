
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class WalkComent extends Coment {

	private Integer	rating;

	// USUARIO QUE HA PUBLICADO EL COMENTARIO/VALORACIÓN (PUEDE SER TANTO OWNER, VET
	// O ADMIN)
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "username")
	private User	user;

	// PASEO AL QUE SE LE HACE EL COMENTARIO/VALORACIÓN
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "walk_id")
	private Walk	walk;


	public Integer getRating() {
		return this.rating;
	}

	public void setRating(final Integer rating) {
		this.rating = rating;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	public Walk getWalk() {
		return this.walk;
	}

	public void setWalk(final Walk walk) {
		this.walk = walk;
	}

}
