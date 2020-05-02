
package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.stereotype.Service;

@DisplayName("Walks Service Tests")
@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WalkServiceTests {

	@Autowired
	protected WalkService walkService;

	@Test
	void shouldFindWalks() {
		Iterable<Walk> walks = this.walkService.findAllWalks();
		Assertions.assertTrue(walks != null);
	}

	@Test
	void shouldFindWalkById() {
		Walk walk = this.walkService.findWalkById(1);
		Assertions.assertTrue(walk != null);
	}

	@Test
	void shouldNotFindWalkById() {
		Walk walk = this.walkService.findWalkById(-1);
		Assertions.assertTrue(walk == null);
	}

	@Test
	void shouldSaveWalks() {
		Walk walk = new Walk();
		walk.setName("Paseo");
		walk.setDescription("This is a try description");
		walk.setMap("https://tinyurl.com/wygb5vu");
		this.walkService.saveWalk(walk);
		Assertions.assertTrue(walk != null);
	}

	@Test
	void shouldDeleteWalk() {
		Walk walk = new Walk();
		walk.setName("Paseo");
		walk.setDescription("This is a try description");
		walk.setMap("https://tinyurl.com/wygb5vu");
		this.walkService.saveWalk(walk);

		Assertions.assertTrue(walk.getId() != 0);

		this.walkService.deleteWalk(walk.getId());
		Assertions.assertTrue(this.walkService.findWalkById(walk.getId()) == null);
	}

	@Test
	void shouldNotDeleteWalk() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.walkService.deleteWalk(99));
	}

	// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "1", "2", "3" })
	void shouldFindWalksParameterized() {
		Iterable<Walk> walks = this.walkService.findAllWalks();
		Assertions.assertTrue(walks != null);
	}

	@ParameterizedTest
	@CsvSource({ "1", "2", "3" })
	void shouldFindWalkByIdParametrized(final Integer walkID) {
		Walk walk = this.walkService.findWalkById(walkID);
		Assertions.assertTrue(walk != null);
	}

	@ParameterizedTest
	@CsvSource({ "111", "222", "333" })
	void shouldNotFindWalkByIdParametrized(final Integer walkID) {
		Walk walk = this.walkService.findWalkById(walkID);
		Assertions.assertTrue(walk == null);
	}

	@ParameterizedTest
	@CsvSource({ "nuevopaseo1, descripcion3, http://www.nuevaurl3.com",
			"NUEVOPASEO2, DESCRIPCION2, http://www.NUEVAURL3.com",
			"nuevoPASEO3, descriPCION3, http://www.NUEvaurl2.com" })
	void shouldSaveWalks(final String name, final String description, final String urlMap) {
		Walk walk = new Walk();
		walk.setName(name);
		walk.setDescription(description);
		walk.setMap(urlMap);
		this.walkService.saveWalk(walk);
		Assertions.assertTrue(walk != null);
	}

	@ParameterizedTest
	@CsvSource({ "nuevopaseo1, descripcion3, http://www.nuevaurl3.com",
			"NUEVOPASEO2, DESCRIPCION2, http://www.NUEVAURL3.com",
			"nuevoPASEO3, descriPCION3, http://www.NUEvaurl2.com" })
	void shouldDeleteWalkParameterized(final String name, final String description, final String urlMap) {
		Walk walk = new Walk();
		walk.setName(name);
		walk.setDescription(description);
		walk.setMap(urlMap);
		this.walkService.saveWalk(walk);

		Assertions.assertTrue(walk.getId() != 0);

		this.walkService.deleteWalk(walk.getId());
		Assertions.assertTrue(this.walkService.findWalkById(walk.getId()) == null);
	}

	@ParameterizedTest
	@CsvSource({ "111", "222", "333" })
	void shouldNotDeleteWalkParameterized(final Integer walkId) {
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> this.walkService.deleteWalk(walkId));
	}
}
