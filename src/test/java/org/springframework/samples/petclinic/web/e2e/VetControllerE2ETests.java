package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link VetController}
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class VetControllerE2ETests {

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testShowVetListHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("vets"))
				.andExpect(MockMvcResultMatchers.view().name("vets/vetList"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitNewVetForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessNewVetFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", "Lucas").param("lastName", "Romero Cruz").param("user.username", "lucromcru")
				.param("user.password", "123456").param("user.email", "email@bien.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/vets/"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessNewVetFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/vets/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("firstName", "").param("lastName", "").param("user.username", "")
						.param("user.password", "").param("user.email", ""))
				.andExpect(MockMvcResultMatchers.model().attributeExists("vet"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("vet"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vet", "firstName"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vet", "lastName"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vet", "user.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vet", "user.password"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vet", "user.email"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vets/createOrUpdateVetForm"));
	}

}
