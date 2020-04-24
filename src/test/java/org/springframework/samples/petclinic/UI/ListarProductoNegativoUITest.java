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
public class ListarProductoNegativoUITest {

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
	public void testListarProductoNegativoUI() throws Exception {
		driver.get("http://localhost:" + port);
		
		loggingAdmin();
		
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
	
	public void loggingAdmin() {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3r");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		assertEquals("OWNER1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}
	
	public void completeForm() {
		driver.findElement(By.id("ProductId")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("limpieza de mascotas");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	public void assertElements() {
		assertTrue(isElementPresent(By.id("Mensaje no hay producto")));
	}
}
