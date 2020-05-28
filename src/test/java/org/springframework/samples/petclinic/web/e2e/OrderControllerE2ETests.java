package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrderControllerE2ETests {

	private static final int TEST_ORDER_ID = 1;
	private static final int TEST_ORDER_ID_2 = 2;
	private static final int TEST_OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testShowAllOrders() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/list"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
				.andExpect(MockMvcResultMatchers.view().name("orders/ordersList"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testShowOrderSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", OrderControllerE2ETests.TEST_ORDER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
				.andExpect(MockMvcResultMatchers.view().name("orders/ordersDetails"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", authorities = { "admin" })
	@Test
	void testDeleteOrderSuccess() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/orders/delete/{orderId}", OrderControllerE2ETests.TEST_ORDER_ID_2))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("okMessage"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
				.andExpect(MockMvcResultMatchers.view().name("orders/ordersList"));

	}

}
