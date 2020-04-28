/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * OwnerServiceTests#clinicService clinicService}</code> instance variable,
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OwnerServiceTests {

	@Autowired
	protected OwnerService ownerService;

	@Test
	void shouldFindOwnersByLastName() {
		Collection<Owner> owners = this.ownerService.findOwnerByLastName("Davis");
		Assertions.assertThat(owners.size()).isEqualTo(2);

		owners = this.ownerService.findOwnerByLastName("Daviss");
		Assertions.assertThat(owners.isEmpty()).isTrue();
	}

	@Test
	void shouldFindSingleOwnerWithPet() {
		Owner owner = this.ownerService.findOwnerById(1);
		Assertions.assertThat(owner.getLastName()).startsWith("Blanco");
		Assertions.assertThat(owner.getPets().size()).isEqualTo(1);
		Assertions.assertThat(owner.getPets().get(0).getType()).isNotNull();
		Assertions.assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
	}

	@Test
	@Transactional
	public void shouldInsertOwner() throws DataAccessException, DuplicatedUsernameException {
		Collection<Owner> owners = this.ownerService.findOwnerByLastName("Schultz");
		int found = owners.size();

		Owner owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		owner.setUser(user);

		this.ownerService.saveOwner(owner);
		Assertions.assertThat(owner.getId().longValue()).isNotEqualTo(0);

		owners = this.ownerService.findOwnerByLastName("Schultz");
		Assertions.assertThat(owners.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	void shouldUpdateOwner() throws DataAccessException, DuplicatedUsernameException {
		Owner owner = this.ownerService.findOwnerById(1);
		String oldLastName = owner.getLastName();
		String newLastName = oldLastName + "X";

		owner.setLastName(newLastName);
		this.ownerService.saveOwner(owner);

		// retrieving new name from database
		owner = this.ownerService.findOwnerById(1);
		Assertions.assertThat(owner.getLastName()).isEqualTo(newLastName);
	}

	@Test
	void shouldFindOwnerByUsername() {
		Owner owner = this.ownerService.findOwnerByUsername("prueba");
		Assertions.assertThat(owner != null);
	}

	@Test
	void shouldNotFindOwnerByUsername() {
		Owner owner = this.ownerService.findOwnerByUsername("ownerquenoexiste");
		Assertions.assertThat(owner == null);
	}

	@Test
	void shouldFindOwnerById() {
		Owner owner = this.ownerService.findOwnerById(1);
		Assertions.assertThat(owner != null);
	}

	@Test
	void shouldNotFindOwnerById() {
		Owner owner = this.ownerService.findOwnerById(1242194);
		Assertions.assertThat(owner == null);
	}
	// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "Javier,Romero,Direccion1,Ciudad1,111222333,Username1,Password1,Email1",
			"Alejandro,Blanco,Direccion2,Ciudad2,444555666,Username2,Password2,Email2",
			"Carlos,Cruz,Direccion3,Ciudad3,777888999,Username3,Password3,Email3" })
	public void shouldInsertMultipleOwnersParamtrized(final String firstName, final String lastName,
			final String address, final String city, final String telephone, final String username,
			final String password, final String email) throws DataAccessException, DuplicatedUsernameException {
		Collection<Owner> owners = this.ownerService.findOwnerByLastName(lastName);
		int found = owners.size();

		Owner owner = new Owner();
		owner.setFirstName(firstName);
		owner.setLastName(lastName);
		owner.setAddress(address);
		owner.setCity(city);
		owner.setTelephone(telephone);
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEnabled(true);
		user.setEmail(email);
		owner.setUser(user);

		this.ownerService.saveOwner(owner);
		Assertions.assertThat(owner.getId().longValue()).isNotEqualTo(0);

		owners = this.ownerService.findOwnerByLastName(lastName);
		Assertions.assertThat(owners.size()).isEqualTo(found + 1);
	}

	@ParameterizedTest
	@CsvSource({ "owner1", "prueba" })
	void shouldFindOwnersByUsernameParametrized(final String username) {
		Owner owner = this.ownerService.findOwnerByUsername(username);
		Assertions.assertThat(owner != null);
	}

	@ParameterizedTest
	@CsvSource({ "owner123", "prueba123", "ownerQueNoExiste" })
	void shouldNotFindOwnersByUsernameParametrized(final String username) {
		Owner owner = this.ownerService.findOwnerByUsername(username);
		Assertions.assertThat(owner == null);
	}

	@ParameterizedTest
	@CsvSource({ "1", "2", "3", "4", "5" })
	void shouldFindOwnerByIdParametrized(final int id) {
		Owner owner = this.ownerService.findOwnerById(id);
		Assertions.assertThat(owner != null);
	}

	@ParameterizedTest
	@CsvSource({ "199", "299", "399", "499", "599" })
	void shouldNotFindOwnerByIdParametrized(final int id) {
		Owner owner = this.ownerService.findOwnerById(id);
		Assertions.assertThat(owner != null);
	}
}
