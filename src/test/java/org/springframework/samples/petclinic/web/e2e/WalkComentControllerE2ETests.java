package org.springframework.samples.petclinic.web.e2e;

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
public class WalkComentControllerE2ETests {

	private static final int TEST_WALK_ID = 1;
	private static final int TEST_WALKCOMENT_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testSaveWalkComent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/walks/{walkId}/add-walk-coment", WalkComentControllerE2ETests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "Title")
						.param("description", "This is a description").param("rating", "2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testSaveWalkComentEmptyInsert() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/walks/{walkId}/add-walk-coment", WalkComentControllerE2ETests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("description", "")
						.param("rating", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walkComent"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testSaveWalkComentNoRating() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/walks/{walkId}/add-walk-coment", WalkComentControllerE2ETests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("title", "").param("description", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walkComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walkComent", "rating"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = "admin")
	@Test
	void testDeleteWalkComent() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/walks/" + WalkComentControllerE2ETests.TEST_WALK_ID
						+ "/walkComents/" + WalkComentControllerE2ETests.TEST_WALKCOMENT_ID + "/delete"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/walks/{walkId}"));
	}

	// walks/{walkId}/walkComents/{walkComentId}/delete

}
