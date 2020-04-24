package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AñadirProductoNegativoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAñadirProductoNegativoUI() throws Exception {
		driver.get("http://localhost:" + port);

		loginAdmin();

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

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public void loginAdmin() throws Exception {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		assertEquals("ADMIN1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		driver.findElement(By.id("AdminId")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Añadir Producto')]")).click();

		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("Titulo caso Negativo");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description"))
				.sendKeys("esto es una descripción para un caso negativo de añadir producto");
		driver.findElement(By.name("stock")).clear();
		driver.findElement(By.name("stock")).sendKeys("60");
		driver.findElement(By.id("urlImage")).clear();
		driver.findElement(By.id("urlImage")).sendKeys("https://images.app.goo.gl/8336dBbsrkshuMvo8");
		new Select(driver.findElement(By.id("category"))).selectByVisibleText("HYGIENE");
		driver.findElement(By.xpath("//option[@value='HYGIENE']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void assertElements() throws Exception {
		assertTrue(isElementPresent(By.xpath("//form[@id='add-product-form']/div/div[5]/div/span[2]")));
	}
}
