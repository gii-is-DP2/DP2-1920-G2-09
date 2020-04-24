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
public class ValoraProductoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	private Double valoracionFinal;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Disabled
	@Test
	public void testValoraProductoUI() throws Exception {
		driver.get("http://localhost:" + port);

		loggingOwner();
		
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
	
	public void loggingOwner() {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3r");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	}
	
	public void completeForm() {
		driver.findElement(By.id("ProductId")).click();

		driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();

		Double valoracionInicial = Double
				.valueOf(driver.findElement(By.id("RatingProducto")).getText().replace(",", "."));
		Integer valoracion = 4;
		new Select(driver.findElement(By.id("rating"))).selectByVisibleText(String.valueOf(valoracion));
		valoracionFinal = (valoracionInicial + valoracion) / 2;
		driver.findElement(By.xpath("//option[@value='4']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	public void assertElements() {
		assertEquals(valoracionFinal,
				Double.valueOf(driver.findElement(By.id("RatingProducto")).getText().replace(",", ".")));
	}
}
