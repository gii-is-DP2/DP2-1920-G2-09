package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.web.OwnerController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link OwnerController}
 *
 * @author Colin But
 */

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
class OwnerControllerE2ETests {

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_OWNER_ID_2 = 2;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/new").param("firstName", "Joe")
				.param("lastName", "Bloggs").with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("address", "123 Caramel Street").param("city", "London").param("telephone", "01316761638"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("firstName", "Joe").param("lastName", "Bloggs").param("city", "London"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitFindForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/find"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessFindFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("owners/ownersList"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessFindFormByLastName() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Blanco"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(
						MockMvcResultMatchers.view().name("redirect:/owners/" + OwnerControllerE2ETests.TEST_OWNER_ID));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessFindFormNoOwnersFound() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners").param("lastName", "Unknown Surname"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "lastName"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("owner", "lastName", "notFound"))
				.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitUpdateOwnerForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/edit", OwnerControllerE2ETests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("lastName", Matchers.is("Blanco"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("firstName", Matchers.is("Alejandro"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("city", Matchers.is("Madison"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessUpdateOwnerFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerE2ETests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joaquin")
						.param("lastName", "Asturias").param("address", "123 Caramel Street").param("city", "London")
						.param("telephone", "01616291589").param("user.username", "hola").param("user.password", "1233")
						.param("user.email", "joaquin@gmail.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessUpdateOwnerFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/edit", OwnerControllerE2ETests.TEST_OWNER_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("firstName", "Joe")
						.param("lastName", "Bloggs").param("city", "London"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "address"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "telephone"))
				.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testShowOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}", OwnerControllerE2ETests.TEST_OWNER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("lastName", Matchers.is("Blanco"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("firstName", Matchers.is("Alejandro"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("address", Matchers.is("110 W. Liberty St."))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("city", Matchers.is("Madison"))))
				.andExpect(MockMvcResultMatchers.model().attribute("owner",
						Matchers.hasProperty("telephone", Matchers.is("6085551023"))))
				.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"));
	}

	// LAS PRUEBAS NUEVAS A PARTIR DE AQUÍ
	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testInitPaymentDetailsForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/payment-details"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.view().name("/owners/paymentDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testProcessPaymentDetailsFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/payment-details")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("creditCardNumber", "1111222233334444")
						.param("cvv", "123").param("expirationYear", "2040").param("expirationMonth", "03"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("OKmessage"))
				.andExpect(MockMvcResultMatchers.view().name("/owners/paymentDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testPaymentDetailsFormHasErrorsInsertingNothing() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/payment-details")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("owner", "expirationYear"))
				.andExpect(MockMvcResultMatchers.view().name("/owners/paymentDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testShowOwnerProfile() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/profile"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("prescriptions"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
				.andExpect(MockMvcResultMatchers.model().attribute("owner", Matchers.hasProperty("pets")))
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerProfile"));
	}

}
