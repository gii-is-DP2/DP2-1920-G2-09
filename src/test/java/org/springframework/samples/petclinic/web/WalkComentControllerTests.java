package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Walk;
import org.springframework.samples.petclinic.model.WalkComent;
import org.springframework.samples.petclinic.service.WalkComentService;
import org.springframework.samples.petclinic.service.WalkService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = WalkComentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
class WalkComentControllerTests {
	
	private static final int TEST_WALK_ID = 1;
	private static final int TEST_WALKCOMENT_ID = 1;

	@MockBean
	private WalkComentService walkComentService;
	@MockBean
	private WalkService walkService;

	@Autowired
	private MockMvc mockMvc;
	
	private User mockUser;
	private Walk mockWalk;
	private WalkComent mockWalkComent;

	@BeforeEach
	void setup() {
		
		this.mockUser = new User();
		
		this.mockWalk = new Walk();
		this.mockWalk.setId(WalkComentControllerTests.TEST_WALK_ID);
		this.mockWalk.setName("mockwalk");
		this.mockWalk.setDescription("This is a wonderful Island of riches and technology");
		this.mockWalk.setMap("https://tinyurl.com/wygb5vu");
		
		this.mockWalkComent = new WalkComent();
		this.mockWalkComent.setId(WalkComentControllerTests.TEST_WALKCOMENT_ID);
		this.mockWalkComent.setTitle("Mock Coment");
		this.mockWalkComent.setDescription("Description for a walk coment");
		this.mockWalkComent.setPostDate(LocalDate.of(2019, 05, 14));
		this.mockWalkComent.setRating(4);
		this.mockWalkComent.setUser(this.mockUser);
		this.mockWalkComent.setWalk(this.mockWalk);
		
		BDDMockito.given(this.walkService.findWalkById(WalkComentControllerTests.TEST_WALK_ID)).willReturn(this.mockWalk);

	}
	
    @WithMockUser(username = "mock", password = "1234", roles = "veterinarian")
    @Test
    void testSaveWalkComent() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders
			.post("/walks/{walkId}/add-walk-coment", WalkComentControllerTests.TEST_WALK_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("title", "Title")
			.param("description", "This is a description")
			.param("rating", "2"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
    }
    
    @WithMockUser(username = "mock", password = "1234", roles = "veterinarian")
    @Test
    void testSaveWalkComentEmptyInsert() throws Exception {
	this.mockMvc
		.perform(MockMvcRequestBuilders
			.post("/walks/{walkId}/add-walk-coment", WalkComentControllerTests.TEST_WALK_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("title", "")
			.param("description", "")
			.param("rating", ""))
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walkComent"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
    }
    
    @WithMockUser(username = "mock", password = "1234", roles = "veterinarian")
    @Test
    void testSaveWalkComentNoRating() throws Exception {
	this.mockMvc
		.perform(MockMvcRequestBuilders
			.post("/walks/{walkId}/add-walk-coment", WalkComentControllerTests.TEST_WALK_ID)
			.with(SecurityMockMvcRequestPostProcessors.csrf())
			.param("title", "")
			.param("description", ""))
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("walkComent"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("walkComent", "rating"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("walks/walkDetails"));
    }
    
    @WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
    @Test
    void testDeleteWalkComent() throws Exception {

		
		this.mockMvc.perform(MockMvcRequestBuilders.get("/walks/"+TEST_WALK_ID+"/walkComents/"+TEST_WALKCOMENT_ID + "/delete"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/walks/{walkId}"));
    }
    
    //walks/{walkId}/walkComents/{walkComentId}/delete
	
}
