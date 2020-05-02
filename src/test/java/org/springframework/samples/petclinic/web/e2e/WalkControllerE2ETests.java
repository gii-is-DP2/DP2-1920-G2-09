package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class WalkControllerE2ETests {

	private static final int TEST_WALK_ID = 1;
	private static final int TEST_WALK_ID_2 = 2;
	private static final int TEST_WALK_ID_3 = 3;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testFindAllWalks() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/all")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("walks"))
				.andExpect(MockMvcResultMatchers.view().name("walks/listWalks"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testShowWalk() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/{walkId}", WalkControllerE2ETests.TEST_WALK_ID_2))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("name", Matchers.is("Segundo Paseo"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("description", Matchers.is("Esto es un paseo2"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("map", Matchers.is("https://tinyurl.com/wygb5vu"))))
				.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/walks/new").param("name", "Notmockwalk")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "This is not an Island of riches").param("map", "https://tinyurl.com/wygbvu"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/walks/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "Notmockwalk"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walk"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "map"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "description"))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testInitUpdateWalkForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/{walkId}/edit", WalkControllerE2ETests.TEST_WALK_ID_2))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("description", Matchers.is("Esto es un paseo2"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("name", Matchers.is("Segundo Paseo"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("map", Matchers.is("https://tinyurl.com/wygb5vu"))))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateWalkFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/walks/{walkId}/edit", WalkControllerE2ETests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "LaIsla")
						.param("description", "Bloggers everywhere").param("map", "https://tinyurl.com/wygb5vu"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/walks/{walkId}"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testProcessUpdateWalkFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/walks/{walkId}/edit", WalkControllerE2ETests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Joe")
						.param("available", "false"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walk"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "map"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "description"))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testDeleteWalkComent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/" + WalkControllerE2ETests.TEST_WALK_ID_3 + "/delete"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.view().name("walks/listWalks"));
	}

}
