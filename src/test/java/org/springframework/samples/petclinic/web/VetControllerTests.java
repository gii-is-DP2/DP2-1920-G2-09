package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.hamcrest.xml.HasXPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link VetController}
 */
@WebMvcTest(controllers = VetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class VetControllerTests {

	@Autowired
	private VetController vetController;

	@MockBean
	private VetService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Vet james = new Vet();
		james.setFirstName("James");
		james.setLastName("Carter");
		james.setId(1);
		Vet helen = new Vet();
		helen.setFirstName("Helen");
		helen.setLastName("Leary");
		helen.setId(2);
		Specialty radiology = new Specialty();
		radiology.setId(1);
		radiology.setName("radiology");
		helen.addSpecialty(radiology);
		BDDMockito.given(this.clinicService.findVets()).willReturn(Lists.newArrayList(james, helen));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListHtml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("vets"))
				.andExpect(MockMvcResultMatchers.view().name("vets/vetList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowVetListXml() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets.xml").accept(MediaType.APPLICATION_XML))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_XML_VALUE))
				.andExpect(MockMvcResultMatchers.content().node(HasXPath.hasXPath("/vets/vetList[id=1]/id")));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewVetForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewVetFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/vets/new").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("firstName", "Lucas").param("lastName", "Romero Cruz").param("user.username", "lucromcru")
				.param("user.password", "123456").param("user.email", "email@bien.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/vets/"));
	}

	@WithMockUser(value = "spring")
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
