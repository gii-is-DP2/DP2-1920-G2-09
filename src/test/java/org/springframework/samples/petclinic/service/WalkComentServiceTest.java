
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class WalkComentServiceTest {

	@Autowired
	protected WalkComentService walkComentService;
	@Autowired
	protected WalkService walkService;

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
		User nu = new User();
		nu.setEnabled(true);
		nu.setPassword("contraseña");
		nu.setUsername("u");
		wC.setDescription("Descripcion de prueba");
		wC.setPostDate(LocalDate.now().minusDays(2));
		wC.setWalk(w);
		wC.setRating(4);
		wC.setTitle("Esto es un titulo");
		wC.setUser(nu);
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
	
    @Test
    void shouldSaveWalkComents() {
    	User nu = new User();
		nu.setEnabled(true);
		nu.setPassword("contraseña");
		nu.setUsername("u");
    	Walk walk = new Walk();
    	WalkComent walkComent = new WalkComent();
    	walk.setName("Paseo");
    	walk.setDescription("This is a try description");
    	walk.setMap("https://tinyurl.com/wygb5vu");
    	this.walkService.saveWalk(walk);
    	walkComent.setTitle("title");
    	walkComent.setDescription("this is a description");
    	walkComent.setRating(5);
    	walkComent.setPostDate(LocalDate.of(2020, 02, 23));
    	walkComent.setUser(nu);
    	walkComent.setWalk(walk);
    	this.walkComentService.saveWalkComent(walkComent);
    	Assertions.assertTrue(walkComent != null);
    }
    
    @Test
    void shouldDeleteWalkComent() {
    	User nu = new User();
		nu.setEnabled(true);
		nu.setPassword("contraseña");
		nu.setUsername("u");
    	Walk walk = new Walk();
    	WalkComent walkComent = new WalkComent();
    	walk.setName("Paseo");
    	walk.setDescription("This is a try description");
    	walk.setMap("https://tinyurl.com/wygb5vu");
    	this.walkService.saveWalk(walk);
    	walkComent.setTitle("title");
    	walkComent.setDescription("this is a description");
    	walkComent.setRating(5);
    	walkComent.setPostDate(LocalDate.of(2020, 02, 23));
    	walkComent.setUser(nu);
    	walkComent.setWalk(walk);
    	this.walkComentService.saveWalkComent(walkComent);
    	Assertions.assertTrue(walkComent.getId() != 0);
    	
    	this.walkComentService.deleteWalkComent(walkComent.getId());
    	Assertions.assertTrue(this.walkComentService.findWalkComentsById(walkComent.getId()) == null);
    }
    
    // PRUEBAS PARAMETRIZADAS
    
    @ParameterizedTest
    @CsvSource({
		"nuevoPASEO1, descripcionDELPASEO1, http://www.nuevaURL.com, nuevaPASS1, nuevoUser1, nuevoEmail1@gmail.com,DescrIPCION DEL COMentario,1,TITULO del comenTario",
		"nuevoPASEO2, DESCRIPCIÓNDELPASEO2, http://www.NUEVAURL2.com, nuevaPASS2, nuevoUser2, nuevoEmail2@gmail.com,DescrIPCION DEL COMentario,5,TITULO del comenTario",
		"nuevopaseo3, descripcion3, http://www.nuevaurl3.com, nuevaPASS3, nuevoUser3, nuevoEmail3@gmail.com ,descripcionDELcomentario,3,titulo del comenTario" })
    void shouldSaveWalkComentsParameterized(final String walkName, final String descripcion, final String urlMap,
			final String password, final String username, final String email, final String comentDescription,
			final Integer rating, final String comentTitle) {
    	User nu = new User();
		nu.setEnabled(true);
		nu.setPassword(password);
		nu.setUsername(username);
    	Walk walk = new Walk();
    	WalkComent walkComent = new WalkComent();
    	walk.setName(walkName);
    	walk.setDescription(descripcion);
    	walk.setMap(urlMap);
    	this.walkService.saveWalk(walk);
    	walkComent.setTitle(comentTitle);
    	walkComent.setDescription(comentDescription);
    	walkComent.setRating(rating);
    	walkComent.setPostDate(LocalDate.now().minusDays(2));
    	walkComent.setUser(nu);
    	walkComent.setWalk(walk);
    	this.walkComentService.saveWalkComent(walkComent);
    	Assertions.assertTrue(walkComent != null);
    }
    
    @ParameterizedTest
    @CsvSource({
		"nuevoPASEO1, descripcionDELPASEO1, http://www.nuevaURL.com, nuevaPASS1, nuevoUser1, nuevoEmail1@gmail.com,DescrIPCION DEL COMentario,1,TITULO del comenTario",
		"nuevoPASEO2, DESCRIPCIÓNDELPASEO2, http://www.NUEVAURL2.com, nuevaPASS2, nuevoUser2, nuevoEmail2@gmail.com,DescrIPCION DEL COMentario,5,TITULO del comenTario",
		"nuevopaseo3, descripcion3, http://www.nuevaurl3.com, nuevaPASS3, nuevoUser3, nuevoEmail3@gmail.com ,descripcionDELcomentario,3,titulo del comenTario" })
    void shouldDeleteWalkComentParamererized(final String walkName, final String descripcion, final String urlMap,
			final String password, final String username, final String email, final String comentDescription,
			final Integer rating, final String comentTitle) {
    	User nu = new User();
		nu.setEnabled(true);
		nu.setPassword(password);
		nu.setUsername(username);
    	Walk walk = new Walk();
    	WalkComent walkComent = new WalkComent();
    	walk.setName(walkName);
    	walk.setDescription(descripcion);
    	walk.setMap(urlMap);
    	this.walkService.saveWalk(walk);
    	walkComent.setTitle(comentTitle);
    	walkComent.setDescription(comentDescription);
    	walkComent.setRating(rating);
    	walkComent.setPostDate(LocalDate.now().minusDays(2));
    	walkComent.setUser(nu);
    	walkComent.setWalk(walk);
    	this.walkComentService.saveWalkComent(walkComent);
    	Assertions.assertTrue(walkComent.getId() != 0);
    	
    	this.walkComentService.deleteWalkComent(walkComent.getId());
    	Assertions.assertTrue(this.walkComentService.findWalkComentsById(walkComent.getId()) == null);
    }


}
