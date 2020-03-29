package org.springframework.samples.petclinic.web;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.service.WalkComentService;
import org.springframework.samples.petclinic.service.WalkService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = WalkController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class WalkControllerTests {

	private static final int TEST_WALK_ID = 1;

	@MockBean
	private WalkService walkService;

	@MockBean
	private WalkComentService walkComentService;

	@Autowired
	private MockMvc mockMvc;

	private Walk mockwalk;

	@BeforeEach
	void setup() {

		this.mockwalk = new Walk();
		this.mockwalk.setId(WalkControllerTests.TEST_WALK_ID);
		this.mockwalk.setName("mockwalk");
		this.mockwalk.setDescription("This is a wonderful Island of riches and technology");
		this.mockwalk.setMap("https://tinyurl.com/wygb5vu");
		BDDMockito.given(this.walkService.findWalkById(WalkControllerTests.TEST_WALK_ID)).willReturn(this.mockwalk);

	}

	@WithMockUser(username = "prueba", password = "prueba", roles = "owner")
	@Test
	void testFindAllWalks() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/all")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("walks"))
				.andExpect(MockMvcResultMatchers.view().name("walks/listWalks"));
	}

	@WithMockUser(username = "prueba", password = "prueba", roles = "owner")
	@Test
	void testShowWalk() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/{walkId}", WalkControllerTests.TEST_WALK_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("name", Matchers.is("mockwalk"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("description",
								Matchers.is("This is a wonderful Island of riches and technology"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("map", Matchers.is("https://tinyurl.com/wygb5vu"))))
				.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/walks/new").param("name", "Notmockwalk")
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "This is not an Island of riches").param("map", "https://tinyurl.com/wygbvu"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
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

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testInitUpdateWalkForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/{walkId}/edit", WalkControllerTests.TEST_WALK_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("walk"))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("description",
								Matchers.is("This is a wonderful Island of riches and technology"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("name", Matchers.is("mockwalk"))))
				.andExpect(MockMvcResultMatchers.model().attribute("walk",
						Matchers.hasProperty("map", Matchers.is("https://tinyurl.com/wygb5vu"))))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testProcessUpdateWalkFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/walks/{walkId}/edit", WalkControllerTests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "LaIsla")
						.param("description", "Bloggers everywhere").param("map", "https://tinyurl.com/wygb5vu"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/walks/{walkId}"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testProcessUpdateWalkFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/walks/{walkId}/edit", WalkControllerTests.TEST_WALK_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Joe")
						.param("available", "false"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walk"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "map"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walk", "description"))
				.andExpect(MockMvcResultMatchers.view().name("walks/createOrUpdateWalkForm"));
	}
	
    @WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
    @Test
    void testDeleteWalkComent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/"+TEST_WALK_ID+"/delete"))
		.andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(MockMvcResultMatchers.view().name("walks/listWalks"));
    }
	
	
	
}
