package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.EmailService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.PrescriptionService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PrescriptionController}
 */
@WebMvcTest(controllers = PrescriptionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class PrescriptionControllerTests {

	private static final int TEST_PRESCRIPTION_ID = 1;

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PrescriptionService clinicService;

	@MockBean
	private VetService vetService;

	@MockBean
	private PetService petService;

	@MockBean
	private EmailService email;

	@BeforeEach
	void setup() {
		Prescription p = new Prescription();

		User user = new User();
		user.setUsername("carcru");
		user.setPassword("1234568");
		user.setEnabled(true);

		Owner owner = new Owner();
		owner.setId(PrescriptionControllerTests.TEST_OWNER_ID);
		owner.setUser(user);
		owner.setAddress("hsha");
		owner.setCity("sevilla");
		owner.setFirstName("ajajaja");
		owner.setLastName("javi");
		owner.setTelephone("988755839");

		Pet pet = new Pet();
		pet.setId(PrescriptionControllerTests.TEST_PET_ID);
		pet.setBirthDate(LocalDate.of(2020, 01, 01));
		pet.setName("pepa");
		PetType cat = new PetType();
		cat.setName("cat");
		pet.setType(cat);
		pet.setOwner(owner);

		User user2 = new User();
		user.setUsername("carcru2");
		user.setPassword("12345628");
		user.setEnabled(true);

		Vet vet = new Vet();

		vet.setId(1);

		vet.setFirstName("carlos");
		vet.setLastName("cruz");
		vet.setUser(user2);

		p.setId(PrescriptionControllerTests.TEST_PRESCRIPTION_ID);

		p.setName("Titulo de Prueba");
		p.setDateInicio(LocalDate.of(2020, 12, 12));
		p.setDateFinal(LocalDate.of(2020, 12, 15));
		p.setDescription("esta es una descripcion de prueba para los test");

		p.setPet(this.petService.findPetById(1));
		p.setVet(this.vetService.findVetbyUser("vet1"));

		List<Prescription> listPrescription = new ArrayList<Prescription>();
		listPrescription.add(p);
		BDDMockito.given(this.clinicService.findPrescriptionsByPetId(PrescriptionControllerTests.TEST_PET_ID))
				.willReturn(listPrescription);

		BDDMockito.given(this.petService.findPetById(PrescriptionControllerTests.TEST_PET_ID)).willReturn(pet);

		BDDMockito.given(this.clinicService.findPrescriptionById(PrescriptionControllerTests.TEST_PRESCRIPTION_ID))
				.willReturn(p);

		BDDMockito.given(this.vetService.findVetbyUser(user2.getUsername())).willReturn(vet);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewPrescriptionForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/new",
						PrescriptionControllerTests.TEST_OWNER_ID, PrescriptionControllerTests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/prescriptions/createOrUpdatePrescriptionForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewPrescriptionFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/prescriptions/new",
								PrescriptionControllerTests.TEST_OWNER_ID, PrescriptionControllerTests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("dateInicio", "2020/10/15")
						.param("dateFinal", "2020/10/20").param("name", "Titulo")
						.param("description", "Una gran descripcion para la prescription"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewPrescriptionFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/prescriptions/new",
								PrescriptionControllerTests.TEST_OWNER_ID, PrescriptionControllerTests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("dateInicio", "")
						.param("dateFinal", "").param("name", "").param("description", ""))
				.andExpect(MockMvcResultMatchers.model().attributeExists("prescription"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("prescription"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("prescription", "dateInicio"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("prescription", "dateFinal"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("prescription", "name"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("prescription", "description"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/prescriptions/createOrUpdatePrescriptionForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFindAllPrescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/list",
						PrescriptionControllerTests.TEST_OWNER_ID, PrescriptionControllerTests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
				.andExpect(MockMvcResultMatchers.view().name("prescriptions/prescriptionsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowPrescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}",
						PrescriptionControllerTests.TEST_OWNER_ID, PrescriptionControllerTests.TEST_PET_ID,
						PrescriptionControllerTests.TEST_PRESCRIPTION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("prescription"))
				.andExpect(MockMvcResultMatchers.view().name("prescriptions/prescriptionDetails"));
	}

}
