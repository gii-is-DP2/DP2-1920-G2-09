package org.springframework.samples.petclinic.web.e2e;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ProductControllerE2ETest {

	private static final int TEST_PRODUCT_ID = 1;
	private static final int TEST_PRODUCT_ID_2 = 2;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testFindAllProducts() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/all"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testFindFilteredProducts() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/filter"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testShowProductSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/{productId}", ProductControllerE2ETest.TEST_PRODUCT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testShowProductError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", -4))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("available", "true").param("category", "ACCESORY")
						.param("description", "Esta es una descripción de prueba").param("name", "Nombre")
						.param("stock", "12").param("unitPrice", "12.20").param("urlImage", "https://www.url.com"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.requestAttr("available", true).requestAttr("category", Category.ACCESORY)
						.requestAttr("stock", 12).param("name", "Nombre").requestAttr("unitPrice", 12.20)
						.param("urlImage", "url.com").param("description", ""))
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("product"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("product", "description"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/{productId}/edit",
						ProductControllerE2ETest.TEST_PRODUCT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("id", Matchers.is(ProductControllerE2ETest.TEST_PRODUCT_ID))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("available", Matchers.is(true))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("category", Matchers.is(Category.FOOD))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("description", Matchers.is("Es un gel de perro"))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("name", Matchers.is("Gel de perro"))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("unitPrice", Matchers.is(10.20))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("urlImage", Matchers.is("https://tinyurl.com/vp4wlrz"))))
				.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/edit", ProductControllerE2ETest.TEST_PRODUCT_ID_2)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("available", "true")
						.param("description", "OTRA DESCRIPCION DE MAS DE 20 CARACTERES").param("name", "OTRO NOMBRE")
						.param("stock", "12000").param("unitPrice", "12.20").param("category", "FOOD")
						.param("urlImage", "https://www.url.com"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}
}
