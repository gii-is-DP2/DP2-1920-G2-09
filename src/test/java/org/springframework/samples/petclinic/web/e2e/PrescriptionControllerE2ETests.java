package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.PrescriptionController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PrescriptionController}
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class PrescriptionControllerE2ETests {

	private static final int TEST_PRESCRIPTION_ID = 1;

	private static final int TEST_OWNER_ID = 11;
	private static final int TEST_PET_ID = 13;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testInitNewPrescriptionForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/new",
						PrescriptionControllerE2ETests.TEST_OWNER_ID, PrescriptionControllerE2ETests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/prescriptions/createOrUpdatePrescriptionForm"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessNewPrescriptionFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/prescriptions/new",
								PrescriptionControllerE2ETests.TEST_OWNER_ID,
								PrescriptionControllerE2ETests.TEST_PET_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("dateInicio", "2020/10/15")
						.param("dateFinal", "2020/10/20").param("name", "Titulo")
						.param("description", "Una gran descripcion para la prescription"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testProcessNewPrescriptionFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/{ownerId}/pets/{petId}/prescriptions/new",
								PrescriptionControllerE2ETests.TEST_OWNER_ID,
								PrescriptionControllerE2ETests.TEST_PET_ID)
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

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testFindAllPrescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/list",
						PrescriptionControllerE2ETests.TEST_OWNER_ID, PrescriptionControllerE2ETests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("selections"))
				.andExpect(MockMvcResultMatchers.view().name("prescriptions/prescriptionsList"));
	}

	@WithMockUser(username = "vet1", password = "v3t", authorities = "veterinarian")
	@Test
	void testShowPrescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/prescriptions/{prescriptionId}",
						PrescriptionControllerE2ETests.TEST_OWNER_ID, PrescriptionControllerE2ETests.TEST_PET_ID,
						PrescriptionControllerE2ETests.TEST_PRESCRIPTION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("prescription"))
				.andExpect(MockMvcResultMatchers.view().name("prescriptions/prescriptionDetails"));
	}

}
