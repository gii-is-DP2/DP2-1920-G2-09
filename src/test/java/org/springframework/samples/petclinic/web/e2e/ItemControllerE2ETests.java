
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-mysql.properties")
public class ItemControllerE2ETests {

	private static final int TEST_ITEM_ID = 1;
	private static final int TEST_ITEM_ID_2 = 2;
	private static final int TEST_ITEM_ID_3 = 3;
	private static final int TEST_PRODUCT_ID = 1;
	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testAddItemToShoppingCartSucces() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/add-item/{productId}", ItemControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "2"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testAddItemToShoppingCartFailure() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/add-item/{productId}", ItemControllerE2ETests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "200"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testDeleteItemSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/delete-item/{itemId}",
						ItemControllerE2ETests.TEST_ITEM_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/shopping-cart"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = "owner")
	@Test
	void testEditItemSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/edit-item/{itemId}", ItemControllerE2ETests.TEST_ITEM_ID_2)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "7"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/shopping-cart"));
	}

}
