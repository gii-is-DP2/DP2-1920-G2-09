
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WalkComentServiceTest {

	@Autowired
	protected WalkComentService walkComentService;

	@Test
	void shouldFindWalkComents() {
		Collection<WalkComent> walkComents = this.walkComentService.findAllComentsOfTheWalk(1);
		Assertions.assertTrue(!walkComents.isEmpty());
	}

	@Test
	void shouldNotFindWalkComents() {
		Collection<WalkComent> walkComents = this.walkComentService.findAllComentsOfTheWalk(-1);
		Assertions.assertTrue(walkComents.isEmpty());
	}

	@Test
	void shouldFindUserByUsername() {
		User user = this.walkComentService.findUserByUsername("prueba");
		Assertions.assertTrue(user != null);

	}

	@Test
	void shouldNotFindUserByUsername() {
		User user = this.walkComentService.findUserByUsername("");
		Assertions.assertTrue(user == null);

	}

	@Test
	void shouldInsertWalkComent() {
		WalkComent wC = new WalkComent();
		Walk w = new Walk();
		w.setName("Producto de prueba");
		w.setDescription("Descripción de prueba");
		w.setMap("http://url.com");
		User u = new User();
		u.setEnabled(true);
		u.setPassword("contraseña");
		u.setUsername("u");
		u.setEmail("email@bien.com");
		wC.setDescription("Descripcion de prueba");
		wC.setPostDate(LocalDate.now().minusDays(2));
		wC.setWalk(w);
		wC.setRating(4);
		wC.setTitle("Esto es un titulo");
		wC.setUser(u);
		this.walkComentService.saveWalkComent(wC);
		Assert.assertTrue(wC.getId().longValue() != 0);
		Assert.assertTrue(this.walkComentService.findAllComentsOfTheWalk(w.getId()).size() == 1);
	}

	@Test
	void shouldGetRatingOfTheProduct() {
		Double rating = this.walkComentService.getAverageRatingOfWalk(1);
		Assert.assertTrue(rating > 0.0);
	}

	@Test
	void shouldNotGetRatingOfTheProduct() {
		Double rating = this.walkComentService.getAverageRatingOfWalk(1231231);
		Assert.assertTrue(rating == 0.0);
	}

}
