package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AñadirACarritoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private String nombre;
	private String descripcion;
	private Integer stock;
	private Double precio;
	private String stockFinal;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAñadirACarritoUI() throws Exception {

		driver.get("http://localhost:" + port);

		loginOwner();

		completeForm();

		assertElements();

	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public void loginOwner() throws Exception {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3r");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		assertEquals("OWNER1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
		driver.findElement(By.id("ProductId")).click();
	}

	public void completeForm() throws Exception {
		driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();

		nombre = driver.findElement(By.id("NombreProducto")).getText();
		descripcion = driver.findElement(By.id("DescripcionProducto")).getText();
		stock = new Integer(driver.findElement(By.id("stockProducto")).getText());
		precio = new Double(driver.findElement(By.id("PrecioProducto")).getText());

		driver.findElement(By.xpath("//input[@name='quantity']")).clear();
		driver.findElement(By.xpath("//input[@name='quantity']")).sendKeys("5");
		driver.findElement(By.xpath("//form[@id='add-product-to-shoppingCart']/button")).click();

		stockFinal = String.valueOf(stock - 5);
	}

	public void assertElements() throws Exception {
		assertEquals(stockFinal, driver.findElement(By.id("stockProducto")).getText());
		driver.findElement(By.linkText("SHOPPING CART")).click();

		Integer lista = driver.findElement(By.id("items")).findElements(By.tagName("tr")).size();
		assertEquals(descripcion,
				driver.findElement(By.xpath("(//td[@id='descriptionProductoCart'])[" + lista + "]")).getText());
		assertEquals("5", driver.findElement(By.xpath("(//td[@id='quantityProductoCart'])[" + lista + "]")).getText());
		assertEquals(String.valueOf(precio) + " euros",
				driver.findElement(By.xpath("(//td[@id='precioProductoCart'])[" + lista + "]")).getText());
		assertEquals(String.valueOf(precio * 5) + " euros",
				driver.findElement(By.xpath("(//td[@id='precioTotalCart'])[" + lista + "]")).getText());
		assertEquals(nombre, driver.findElement(By.xpath("(//td[@id='nameProductoCart'])[" + lista + "]")).getText());
	}
}
