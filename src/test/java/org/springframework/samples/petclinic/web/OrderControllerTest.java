package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ItemService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = OrderController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class OrderControllerTest {

    private static final int TEST_ORDER_ID = 1;
    private static final int TEST_OWNER_ID = 1;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ProductService productService;

    private Order order;

    @BeforeEach
    void setup() {
	this.order = new Order();
	this.order.setId(OrderControllerTest.TEST_ORDER_ID);
	this.order.setOrderDate(LocalDate.now().minusDays(1));
	this.order.setTotalPrice(250.00);
	Owner owner = new Owner();
	owner.setId(OrderControllerTest.TEST_OWNER_ID);
	owner.setFirstName("George");
	owner.setLastName("Franklin");
	owner.setAddress("110 W. Liberty St.");
	owner.setCity("Madison");
	owner.setTelephone("6085551023");
	this.order.setOwner(owner);
	BDDMockito.given(this.orderService.findOrderById(OrderControllerTest.TEST_ORDER_ID))
		.willReturn(this.order);

    }
    
    @WithMockUser(value = "spring")
    @Test
    void testShowAllOrders() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/list"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("orders"))
		.andExpect(MockMvcResultMatchers.view().name("orders/ordersList"));
    }

    @WithMockUser(value = "spring")
    @Test
    void testShowOrderSuccess() throws Exception {
	this.mockMvc.perform(MockMvcRequestBuilders.get("/orders/{orderId}", OrderControllerTest.TEST_ORDER_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("order"))
		.andExpect(MockMvcResultMatchers.view().name("orders/ordersDetails"));
    }
}
