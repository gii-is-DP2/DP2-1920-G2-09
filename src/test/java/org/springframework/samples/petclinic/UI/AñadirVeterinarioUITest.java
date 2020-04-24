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
public class AñadirVeterinarioUITest {

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
	public void testAñadirVeterinarioUI() throws Exception {
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
	
	public void loggingAdmin() {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	}
	
	public void completeForm() {
		driver.findElement(By.linkText("Veterinarians")).click();
		driver.findElement(By.id("AñadirVet")).click();
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys("Adrian");
		driver.findElement(By.id("lastName")).clear();
		driver.findElement(By.id("lastName")).sendKeys("Fernadez");
		driver.findElement(By.xpath("//option[@value='2']")).click();
		driver.findElement(By.id("user.username")).clear();
		driver.findElement(By.id("user.username")).sendKeys("adriu");
		driver.findElement(By.id("user.password")).clear();
		driver.findElement(By.id("user.password")).sendKeys("1234");
		driver.findElement(By.id("user.email")).clear();
		driver.findElement(By.id("user.email")).sendKeys("adriu@us.es");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	public void assertElements() {
		int filas = driver.findElement(By.id("vetsTable")).findElements(By.tagName("tr")).size() - 1;
		assertEquals("Adrian Fernadez", driver.findElement(By.xpath("(//td[@id='NameVet'])[" + filas + "]")).getText());
		assertEquals("surgery", driver.findElement(By.xpath("(//td[@id='specialtyVet'])[" + filas + "]")).getText());
	}
}


