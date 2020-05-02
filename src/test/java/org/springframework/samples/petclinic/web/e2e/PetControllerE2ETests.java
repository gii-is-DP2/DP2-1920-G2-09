package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PetControllerE2ETests {

	private static final int TEST_OWNER_ID = 1;

	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", PetControllerE2ETests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pet"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessCreationFormSuccessVet() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", PetControllerE2ETests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Holly")
						.param("type", "hamster").param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "prueba", password = "pwd", authorities = "owner")
	@Test
	void testProcessCreationFormSuccessOwner() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", PetControllerE2ETests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Excalibur")
						.param("type", "hamster").param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/profile"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerE2ETests.TEST_OWNER_ID,
								PetControllerE2ETests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty")
						.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit",
						PetControllerE2ETests.TEST_OWNER_ID, PetControllerE2ETests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("pet"))
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerE2ETests.TEST_OWNER_ID,
								PetControllerE2ETests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty")
						.param("type", "hamster").param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/edit", PetControllerE2ETests.TEST_OWNER_ID,
								PetControllerE2ETests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty")
						.param("birthDate", "2015/02/12"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

}
