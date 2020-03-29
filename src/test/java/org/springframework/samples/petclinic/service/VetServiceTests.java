/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Set;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following
 * services provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary
 * set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning
 * that we don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
 * which uses autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <li>An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean
 * lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class VetServiceTests {

	@Autowired
	protected VetService vetService;

	@Test
	void shouldFindAllVets() {
		Collection<Vet> vets = (Collection<Vet>) this.vetService.findVets();
		Assertions.assertTrue(!vets.isEmpty());

		Vet vet = EntityUtils.getById(vets, Vet.class, 3);
		Assertions.assertTrue(vet.getLastName().equals("Douglas"));
		Assertions.assertTrue(vet.getNrOfSpecialties() == 2);
		Assertions.assertTrue(vet.getSpecialties().get(0).getName().equals("dentistry"));
		Assertions.assertTrue(vet.getSpecialties().get(1).getName().equals("surgery"));
	}

	@Test
	void shouldFindVetById() {
		Vet vet = this.vetService.findVetById(1);
		Assertions.assertTrue(vet != null);
	}

	@Test
	void shouldFindSpecialtiesById() {
		Integer[] ids = { 1, 2 };
		Set<Specialty> specialties = this.vetService.findSpecialtiesById(ids);
		Assertions.assertTrue(!specialties.isEmpty());
	}

	@Test
	void shouldFindAllSpecialties() {
		Collection<Specialty> specialties = (Collection<Specialty>) this.vetService.findAllSpecialties();
		Assertions.assertTrue(!specialties.isEmpty());
	}

	@Test
	void shouldSaveVet() throws DataAccessException, DuplicatedUsernameException {
		Vet vet = new Vet();
		vet.setFirstName("Juan");
		vet.setLastName("Montes");

		User user = new User();
		user.setUsername("vet25");
		user.setPassword("123456");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		vet.setUser(user);

		this.vetService.saveVet(vet);
		Assert.assertTrue(vet.getId() != 0);
	}

	@Test
	void shouldNotSaveVet() {
		Vet vet = new Vet();
		User user = new User();
		vet.setUser(user);
		Assertions.assertThrows(JpaSystemException.class, () -> this.vetService.saveVet(vet));
	}

	// PRUEBAS PARAMETRIZADAS
	@ParameterizedTest
	@CsvSource({ "2,Leary,radiology", "3,Douglas,dentistry", "5,Stevens,radiology" })
	void shouldFindAllVetsParametrized(final Integer vetId, final String name, final String specialty) {
		Collection<Vet> vets = (Collection<Vet>) this.vetService.findVets();
		Assertions.assertTrue(!vets.isEmpty());

		Vet vet = EntityUtils.getById(vets, Vet.class, vetId);
		Assertions.assertTrue(vet.getLastName().equals(name));
		Assertions.assertTrue(vet.getSpecialties().get(0).getName().equals(specialty));
	}

	@ParameterizedTest
	@CsvSource({ "1,3,6" })
	void shouldFindVetByIdParametrized(final Integer vetId) {
		Vet vet = this.vetService.findVetById(vetId);
		Assertions.assertTrue(vet != null);
	}

	@ParameterizedTest
	@CsvSource({ "1,3,2" })
	void shouldFindSpecialtiesByIdParametrized(final Integer specialtyID) {
		Integer[] ids = { specialtyID };
		Set<Specialty> specialties = this.vetService.findSpecialtiesById(ids);
		Assertions.assertTrue(!specialties.isEmpty());
	}

	@ParameterizedTest
	@CsvSource({ "NUEVOVET, APELLIDONUEVOVEt, usernamenuevo1, password1, email@gmail1.com",
			"nueVOvet, apellidoNUEVOvet, usernamenuevo2, password2, email@gmail2.com",
			"nuevovet, apellidonuevovet, usernamenuevo3, password3, email@gmail3.com" })
	void shouldSaveVetParamtrized(final String name, final String lastName, final String username,
			final String password, final String email) throws DataAccessException, DuplicatedUsernameException {
		Vet vet = new Vet();
		vet.setFirstName(name);
		vet.setLastName(lastName);

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		vet.setUser(user);

		this.vetService.saveVet(vet);
		Assert.assertTrue(vet.getId() != 0);
	}
}
