package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class ProductComent extends Coment {

    private Boolean highlight;

    private Integer rating;

    // USUARIO QUE HA PUBLICADO EL COMENTARIO/VALORACIÓN (PUEDE SER TANTO OWNER, VET
    // O ADMIN)
    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    // PRODUCTO AL QUE SE LE HACE EL COMENTARIO/VALORACIÓN
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Boolean getHighlight() {
	return this.highlight;
    }

    public void setHighlight(final Boolean highlight) {
	this.highlight = highlight;
    }

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

    public Product getProduct() {
	return this.product;
    }

    public void setProduct(final Product product) {
	this.product = product;
    }

}
