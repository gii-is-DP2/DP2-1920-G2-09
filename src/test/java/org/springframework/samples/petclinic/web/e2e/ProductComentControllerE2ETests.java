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
public class ProductComentControllerE2ETests {
	private static final int TEST_PRODUCT_COMENT_ID = 1;
	private static final int TEST_PRODUCT_ID = 2;
	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testSaveProductComentSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment",
								ProductComentControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion 2")
						.param("rating", "3").param("title", "titulo 2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingNothing() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment",
								ProductComentControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "").param("title", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "rating"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingOnlyTitle() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment",
								ProductComentControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "")
						.param("title", "Solo título"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "description"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingOnlyDescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment",
								ProductComentControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Solo Descripcion")
						.param("title", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "title"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testDeleteProductComentSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/" + ProductComentControllerE2ETests.TEST_PRODUCT_ID
						+ "/delete-product-coment/" + ProductComentControllerE2ETests.TEST_PRODUCT_COMENT_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}

}
