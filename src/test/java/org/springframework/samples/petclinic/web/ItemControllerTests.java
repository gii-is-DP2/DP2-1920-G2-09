
package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShoppingCartService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ItemController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ItemControllerTests {

	private static final int TEST_ITEM_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ItemService itemService;

	@MockBean
	private ShoppingCartService shoppingCartService;

	@MockBean
	private ProductService productService;

	@MockBean
	private ProductComentService productComentService;

	private Item i;

	@BeforeEach
	void setup() {
		this.i = new Item();
		this.i.setQuantity(1);
		this.i.setUnitPrice(10.20);
		this.i.setId(ItemControllerTests.TEST_ITEM_ID);
		this.i.setOrder(null);
		Product product = new Product();
		product.setName("Producto de prueba");
		product.setUrlImage("http://www.urldeprueba.com");
		product.setDescription("Descripción de prueba");
		product.setCategory(Category.ACCESORY);
		product.setUnitPrice(10.20);
		product.setStock(20);
		product.setId(1);
		this.i.setProduct(product);
		ShoppingCart shoppingCart = new ShoppingCart();
		Owner owner = new Owner();
		owner.setFirstName("Sam");
		owner.setLastName("Schultz");
		owner.setAddress("4, Evans Street");
		owner.setCity("Wollongong");
		owner.setTelephone("4444444444");
		User user = new User();
		user.setUsername("Sam");
		user.setPassword("supersecretpassword");
		user.setEnabled(true);
		user.setEmail("email@bien.com");
		owner.setUser(user);
		shoppingCart.setOwner(owner);
		shoppingCart.setId(2);
		this.i.setShoppingCart(shoppingCart);
		BDDMockito.given(this.shoppingCartService.getShoppingCartOfUser("Sam")).willReturn(shoppingCart);
		BDDMockito.given(this.productService.findProductById(1)).willReturn(product);
		BDDMockito.given(this.itemService.checkIfItemIsIntheShoppingCart(2, 1)).willReturn(this.i);
		BDDMockito.given(this.itemService.findItemById(ItemControllerTests.TEST_ITEM_ID)).willReturn(this.i);
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testAddItemToShoppingCartSucces() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/add-item/{productId}", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "2"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testAddItemToShoppingCartFailure() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/add-item/{productId}", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "200"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testDeleteItemSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/delete-item/{itemId}", ItemControllerTests.TEST_ITEM_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/shopping-cart"));
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testDeleteItemFailure() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/delete-item/{itemId}", 4))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testEditItemSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/edit-item/{itemId}", ItemControllerTests.TEST_ITEM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "7"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/shopping-cart"));
	}

	@WithMockUser(username = "Sam", password = "supersecretpassword", roles = "owner")
	@Test
	void testEditItemFailure() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/edit-item/{itemId}", ItemControllerTests.TEST_ITEM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("quantity", "50"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("errorMessage"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/owners/ownerShoppingCart"));
	}

}
