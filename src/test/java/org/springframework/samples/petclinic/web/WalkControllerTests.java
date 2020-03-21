package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.service.WalkService;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(controllers=WalkController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
class WalkControllerTests {

	private static final int TEST_WALK_ID = 1;

	@Autowired
	private WalkController walkController;

	@MockBean
	private WalkService walkService;
        
    @MockBean
	private UserService user5Service;
        
    @MockBean
    private AuthoritiesService authoritiesService; 

	@Autowired
	private MockMvc mockMvc;

	private Walk laputa;

	@BeforeEach
	void setup() {

		laputa = new Walk();
		laputa.setId(TEST_WALK_ID);
		laputa.setName("Laputa");
		laputa.setDescription("This is a wonderful Island of riches and technology");
		laputa.setMap("https://tinyurl.com/wygb5vu");
		given(this.walkService.findWalkById(TEST_WALK_ID)).willReturn(laputa);

	}

    @WithMockUser(value = "spring")
	@Test
	void testShowWalk() throws Exception {
		mockMvc.perform(get("/walks/{walkId}", TEST_WALK_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("walk", hasProperty("name", is("Laputa"))))
				.andExpect(model().attribute("walk", hasProperty("description", is("This is a wonderful Island of riches and technology"))))
				.andExpect(model().attribute("walk", hasProperty("map", is("https://tinyurl.com/wygb5vu"))))
				.andExpect(view().name("walks/walkDetails"));
	}
        
    @WithMockUser(value = "spring")
    @Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/walks/new")).andExpect(status().isOk()).andExpect(model().attributeExists("walk"))
				.andExpect(view().name("walks/createOrUpdateWalkForm"));
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/walks/new").param("name", "NotLaputa")
							.with(csrf())
							.param("description", "This is not an Island of riches")
							.param("map", "https://tinyurl.com/wygbvu"))
				.andExpect(status().is2xxSuccessful());
	}

	@WithMockUser(value = "spring")
    @Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/walks/new")
							.with(csrf())
							.param("name", "NotLaputa"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("walk"))
				.andExpect(model().attributeHasFieldErrors("walk", "map"))
				.andExpect(model().attributeHasFieldErrors("walk", "description"))
				.andExpect(view().name("walks/createOrUpdateWalkForm"));
	}
	
    @WithMockUser(value = "spring")
	@Test
	void testInitUpdateWalkForm() throws Exception {
		mockMvc.perform(get("/walks/{walkId}/edit", TEST_WALK_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("walk"))
				.andExpect(model().attribute("walk", hasProperty("description", is("This is a wonderful Island of riches and technology"))))
				.andExpect(model().attribute("walk", hasProperty("name", is("Laputa"))))
				.andExpect(model().attribute("walk", hasProperty("map", is("https://tinyurl.com/wygb5vu"))))
				.andExpect(view().name("walks/createOrUpdateWalkForm"));
	}

      @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateWalkFormSuccess() throws Exception {
		mockMvc.perform(post("/walks/{walkId}/edit", TEST_WALK_ID)
							.with(csrf())
							.param("name", "LaIsla")
							.param("description", "Bloggers everywhere")
							.param("map", "https://tinyurl.com/wygb5vu"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/walks/{walkId}"));
	}

      @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateWalkFormHasErrors() throws Exception {
		mockMvc.perform(post("/walks/{walkId}/edit", TEST_WALK_ID)
							.with(csrf())
							.param("name", "Joe")
							.param("available", "false"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("walk"))
				.andExpect(model().attributeHasFieldErrors("walk", "map"))
				.andExpect(model().attributeHasFieldErrors("walk", "description"))
				.andExpect(view().name("walks/createOrUpdateWalkForm"));
	}
}
