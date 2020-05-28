
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
class ShoppingCartControllerE2ETests {

	private static final int TEST_SHOPPING_CART_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testShowShoppingCartOfOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/shopping-cart"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("items"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}

	@WithMockUser(username = "prueba", password = "prueba", authorities = { "owner" })
	@Test
	void testBuyShoppingCartOfOwnerSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/shopping-cart/buy")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.model().attributeExists("shoppingCartSuccess"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}

	@WithMockUser(username = "owner2", password = "0wn3r", authorities = { "owner" })
	@Test
	void testBuyShoppingCartOfOwnerFaiture() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/shopping-cart/buy")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.model().attributeExists("shoppingCartError"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}

}
