
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.samples.petclinic.model.Item;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ShoppingCart;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ItemService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ShoppingCartService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ShoppingCartController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ShoppingCartControllerTests {

	private static final int	TEST_SHOPPING_CART_ID	= 1;

	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ShoppingCartService	shoppingCartService;

	@MockBean
	private ItemService			itemService;
	
	@MockBean
	private OrderService		orderService;

	private ShoppingCart		sp;

	private Owner owner;
	
	@BeforeEach
	void setup() {
		this.sp = new ShoppingCart();
		this.owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		owner.setCreditCardNumber("1111222233334444");
		owner.setCvv("510");
		owner.setExpirationMonth(02);
		owner.setExpirationYear(2022);
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		owner.setUser(user);
		Item i = new Item();
		i.setQuantity(1);
		i.setUnitPrice(10.20);
		i.setId(1);
		i.setOrder(null);
		Product product = new Product();
		product.setName("Producto de prueba");
		product.setUrlImage("http://www.urldeprueba.com");
		product.setDescription("Descripción de prueba");
		product.setCategory(Category.ACCESORY);
		product.setUnitPrice(10.20);
		product.setStock(20);
		product.setId(1);
		i.setProduct(product);
		i.setShoppingCart(this.sp);
		List<Item> listItems = new ArrayList<>();
		listItems.add(i);
		this.sp.setOwner(owner);
		this.sp.setId(1);
		BDDMockito.given(this.shoppingCartService.getShoppingCartOfUser("Sam")).willReturn(this.sp);
		BDDMockito.given(this.itemService.findItemsInShoppingCart(TEST_SHOPPING_CART_ID)).willReturn(listItems);
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testShowShoppingCartOfOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/shopping-cart")).andExpect(MockMvcResultMatchers.model().attributeExists("items")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}
	
	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testBuyShoppingCartOfOwnerSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart/buy").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.model()
				.attributeExists("shoppingCartSuccess"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}
	
	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testBuyShoppingCartOfOwnerFaiture() throws Exception {
		this.owner.setCreditCardNumber(null);
		this.mockMvc.perform(MockMvcRequestBuilders.post("/shopping-cart/buy").with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.model()
				.attributeExists("shoppingCartError"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}

}
