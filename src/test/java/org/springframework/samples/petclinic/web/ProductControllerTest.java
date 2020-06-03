package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductComentService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class ProductControllerTest {

	private static final int TEST_PRODUCT_ID = 1;

	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService productService;
	@MockBean
	private ProductComentService productComentService;

	private Product product;

	@BeforeEach
	void setup() {
		this.product = new Product();
		this.product.setId(ProductControllerTest.TEST_PRODUCT_ID);
		this.product.setAvailable(true);
		this.product.setCategory(Category.ACCESORY);
		this.product.setDescription("Esta es una descripción de prueba");
		this.product.setName("Nombre del producto");
		this.product.setStock(10);
		this.product.setUnitPrice(12.20);
		this.product.setUrlImage("http://hola.com");
		BDDMockito.given(this.productService.findProductById(ProductControllerTest.TEST_PRODUCT_ID))
				.willReturn(this.product);

	}

	@WithMockUser(value = "spring")
	@Test
	void testFindAllProducts() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/all"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFindFilteredProducts() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/filter"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("products"))
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowProductSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", ProductControllerTest.TEST_PRODUCT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.view().name("products/productDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowProductError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/{productId}", 4))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/products/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/products/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("available", "true").param("category", "ACCESORY")
						.param("description", "Esta es una descripción de prueba").param("name", "Nombre")
						.param("stock", "12").param("unitPrice", "12.20").param("urlImage", "url.com"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("products/listProducts"));
	}

	@WithMockUser(value = "spring")
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

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders.get("/products/{productId}/edit", ProductControllerTest.TEST_PRODUCT_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("product"))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("id", Matchers.is(ProductControllerTest.TEST_PRODUCT_ID))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("available", Matchers.is(true))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("category", Matchers.is(Category.ACCESORY))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("description", Matchers.is("Esta es una descripción de prueba"))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("name", Matchers.is("Nombre del producto"))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("stock", Matchers.is(10))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("unitPrice", Matchers.is(12.20))))
				.andExpect(MockMvcResultMatchers.model().attribute("product",
						Matchers.hasProperty("urlImage", Matchers.is("http://hola.com"))))
				.andExpect(MockMvcResultMatchers.view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders.post("/products/{productId}/edit", ProductControllerTest.TEST_PRODUCT_ID)
								.with(SecurityMockMvcRequestPostProcessors.csrf()).param("available", "true")
								.param("category", "FOOD").param("urlImage", "google.es")
								.param("description", "OTRA DESCRIPCIOÓNasdaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
								.param("name", "OTRO NOMBRE").param("stock", "12").param("unitPrice", "12.20"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/products/{productId}"));
	}
}
