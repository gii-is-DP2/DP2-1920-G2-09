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

import java.time.LocalDate;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedUsernameException;
import org.springframework.samples.petclinic.util.EntityUtils;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetServiceTests {
	@Autowired
	protected PetService petService;

	@Autowired
	protected OwnerService ownerService;

	@Test
	void shouldFindPetWithCorrectId() {
		Pet pet7 = this.petService.findPetById(7);
		Assertions.assertTrue(pet7.getName().startsWith("Samantha"));
		Assertions.assertTrue(pet7.getOwner().getFirstName().equals("Jean"));

	}

	@Test
	void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = this.petService.findPetTypes();

		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
		Assert.assertTrue(petType1.getName().equals("cat"));
		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
		Assert.assertTrue(petType4.getName().equals("snake"));
	}

	@Test
	@Transactional
	public void shouldInsertPetIntoDatabaseAndGenerateId() throws DuplicatedUsernameException {
		Owner owner6 = this.ownerService.findOwnerById(6);
		int found = owner6.getPets().size();

		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		Assert.assertTrue(owner6.getPets().size() == found + 1);

		try {
			this.petService.savePet(pet);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.ownerService.saveOwner(owner6);

		owner6 = this.ownerService.findOwnerById(6);
		Assert.assertTrue(owner6.getPets().size() == found + 1);
		// checks that id has been generated
		Assert.assertTrue(pet.getId() != null);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingPetsWithTheSameName() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Pet pet = new Pet();
		pet.setName("wario");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		try {
			this.petService.savePet(pet);
		} catch (DuplicatedPetNameException e) {
			// The pet already exists!
			e.printStackTrace();
		}

		Pet anotherPetWithTheSameName = new Pet();
		anotherPetWithTheSameName.setName("wario");
		anotherPetWithTheSameName.setType(EntityUtils.getById(types, PetType.class, 1));
		anotherPetWithTheSameName.setBirthDate(LocalDate.now().minusWeeks(2));
		Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
			owner6.addPet(anotherPetWithTheSameName);
			this.petService.savePet(anotherPetWithTheSameName);
		});
	}

	@Test
	@Transactional
	public void shouldUpdatePetName() throws Exception {
		Pet pet7 = this.petService.findPetById(7);
		String oldName = pet7.getName();

		String newName = oldName + "X";
		pet7.setName(newName);
		this.petService.savePet(pet7);

		pet7 = this.petService.findPetById(7);
		Assert.assertTrue(pet7.getName().equals(newName));
	}

	@Test
	@Transactional
	public void shouldThrowExceptionUpdatingPetsWithTheSameName() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Pet pet = new Pet();
		pet.setName("wario");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);

		Pet anotherPet = new Pet();
		anotherPet.setName("waluigi");
		anotherPet.setType(EntityUtils.getById(types, PetType.class, 1));
		anotherPet.setBirthDate(LocalDate.now().minusWeeks(2));
		owner6.addPet(anotherPet);

		try {
			this.petService.savePet(pet);
			this.petService.savePet(anotherPet);
		} catch (DuplicatedPetNameException e) {
			// The pets already exists!
			e.printStackTrace();
		}

		Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
			anotherPet.setName("wario");
			this.petService.savePet(anotherPet);
		});
	}

	@Test
	@Transactional
	public void shouldAddNewVisitForPet() {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getVisits().size();
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription("test");
		this.petService.saveVisit(visit);
		try {
			this.petService.savePet(pet7);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}

		pet7 = this.petService.findPetById(7);
		Assert.assertTrue(pet7.getVisits().size() == found + 1);
		Assert.assertTrue(visit.getId() != null);
	}

	@Test
	void shouldFindVisitsByPetId() throws Exception {
		Collection<Visit> visits = this.petService.findVisitsByPetId(7);
		Assert.assertTrue(visits.size() == 2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		Assert.assertTrue(visitArr[0].getPet() != null);
		Assert.assertTrue(visitArr[0].getDate() != null);
		Assert.assertTrue(visitArr[0].getPet().getId().equals(7));
	}

	// PRUEBAS PARAMETRIZADAS

	@ParameterizedTest
	@CsvSource({ "1, mascota1", "11, mascota3", "5, mascota2" })
	public void shouldInsertPetIntoDatabaseAndGenerateIdParametrized(final int ownerId, final String petName)
			throws DuplicatedUsernameException {
		Owner owner6 = this.ownerService.findOwnerById(ownerId);
		int found = owner6.getPets().size();

		Pet pet = new Pet();
		pet.setName(petName);
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		Assert.assertTrue(owner6.getPets().size() == found + 1);

		try {
			this.petService.savePet(pet);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.ownerService.saveOwner(owner6);

		owner6 = this.ownerService.findOwnerById(6);
		Assert.assertTrue(owner6.getPets().size() == found + 1);
		// checks that id has been generated
		Assert.assertTrue(pet.getId() != null);
	}

	@ParameterizedTest
	@CsvSource({ "1, mascota1,mascota1", "11, mascota2,mascota2", "5, mascota3,mascota3" })
	public void shouldThrowExceptionUpdatingPetsWithTheSameNameParametrized(final int ownerId, final String petName,
			final String petNameDuplicated) {
		Owner owner6 = this.ownerService.findOwnerById(ownerId);
		Pet pet = new Pet();
		pet.setName(petName);
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);

		Pet anotherPet = new Pet();
		anotherPet.setName(petNameDuplicated);
		anotherPet.setType(EntityUtils.getById(types, PetType.class, 1));
		anotherPet.setBirthDate(LocalDate.now().minusWeeks(2));
		owner6.addPet(anotherPet);

		try {
			this.petService.savePet(pet);
			this.petService.savePet(anotherPet);
		} catch (DuplicatedPetNameException e) {
			// The pets already exists!
			e.printStackTrace();
		}

		Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
			anotherPet.setName(petName);
			this.petService.savePet(anotherPet);
		});
	}

	@ParameterizedTest
	@CsvSource({ "1, descripción1", "11, descripcion2", "5, descripcion3" })
	public void shouldAddNewVisitForPetParametrized(final int petId, final String description) {
		Pet pet7 = this.petService.findPetById(petId);
		int found = pet7.getVisits().size();
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription(description);
		this.petService.saveVisit(visit);
		try {
			this.petService.savePet(pet7);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}

		pet7 = this.petService.findPetById(petId);
		Assert.assertTrue(pet7.getVisits().size() == found + 1);
		Assert.assertTrue(visit.getId() != null);
	}

	@ParameterizedTest
	@CsvSource({ "7,8" })
	void shouldFindVisitsByPetIdParametrized(final int petId) throws Exception {
		Collection<Visit> visits = this.petService.findVisitsByPetId(petId);
		Assert.assertTrue(visits.size() == 2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		Assert.assertTrue(visitArr[0].getPet() != null);
		Assert.assertTrue(visitArr[0].getDate() != null);
		Assert.assertTrue(visitArr[0].getPet().getId().equals(petId));
	}
}