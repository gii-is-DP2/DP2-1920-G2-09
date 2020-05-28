package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.model.Category;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.ProductComent;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductComentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class))
class ProductComentControllerTests {
	private static final int TEST_PRODUCT_COMENT_ID = 1;
	private static final int TEST_PRODUCT_ID = 2;
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;
	@MockBean
	private ProductComentService productComentService;

	private Product product;
	private User u;
	private ProductComent pc;

	@BeforeEach
	void setup() {
		this.product = new Product();
		this.product.setId(ProductComentControllerTests.TEST_PRODUCT_ID);
		this.product.setAvailable(true);
		this.product.setCategory(Category.ACCESORY);
		this.product.setDescription("Esta es una descripción de prueba");
		this.product.setName("Nombre del producto");
		this.product.setStock(10);
		this.product.setUnitPrice(12.20);
		this.product.setUrlImage("http://hola.com");
		this.u = new User();
		this.pc = new ProductComent();
		this.pc.setProduct(this.product);
		this.pc.setUser(this.u);
		this.pc.setId(ProductComentControllerTests.TEST_PRODUCT_COMENT_ID);
		this.pc.setDescription("Descripcion");
		this.pc.setHighlight(false);
		this.pc.setPostDate(LocalDate.of(2020, 03, 12));
		this.pc.setRating(1);
		this.pc.setTitle("Titulo");
		BDDMockito.given(this.productService.findProductById(ProductComentControllerTests.TEST_PRODUCT_ID))
				.willReturn(this.product);

	}

	@WithMockUser(username = "prueba", password = "pwd", roles = "owner")
	@Test
	void testSaveProductComentSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment", ProductComentControllerTests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "descripcion 2")
						.param("rating", "3").param("title", "titulo 2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "pwd", roles = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingNothing() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment", ProductComentControllerTests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "").param("title", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "rating"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "pwd", roles = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingOnlyTitle() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment", ProductComentControllerTests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "")
						.param("title", "Solo título"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "description"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "prueba", password = "pwd", roles = "owner")
	@Test
	void testSaveProductComentHasErrorsInsertingOnlyDescription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/products/{productId}/add-product-coment", ProductComentControllerTests.TEST_PRODUCT_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("description", "Solo Descripcion")
						.param("title", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("productComent"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("productComent", "title"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(username = "admin1", password = "4dm1n", roles = "admin")
	@Test
	void testDeleteProductComentSuccess() throws Exception {

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/products/" + ProductComentControllerTests.TEST_PRODUCT_ID
						+ "/delete-product-coment/" + ProductComentControllerTests.TEST_PRODUCT_COMENT_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}

}
