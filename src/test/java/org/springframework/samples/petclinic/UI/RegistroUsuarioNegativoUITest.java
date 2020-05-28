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
 class RegistroUsuarioNegativoUITest {

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
	 void testRegistroUsuarioNegativoUI() throws Exception {
		driver.get("http://localhost:" + port);
		
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
	
	public void completeForm() {
		driver.findElement(By.linkText("REGISTER")).click();
		driver.findElement(By.id("firstName")).clear();
		driver.findElement(By.id("firstName")).sendKeys("Javier");
		driver.findElement(By.id("lastName")).clear();
		driver.findElement(By.id("address")).clear();
		driver.findElement(By.id("address")).sendKeys("Reina Mercedes 24");
		driver.findElement(By.id("city")).clear();
		driver.findElement(By.id("city")).sendKeys("Sevilla");
		driver.findElement(By.id("telephone")).clear();
		driver.findElement(By.id("telephone")).sendKeys("11122233");
		driver.findElement(By.id("user.username")).clear();
		driver.findElement(By.id("user.username")).sendKeys("javier991");
		driver.findElement(By.id("user.password")).clear();
		driver.findElement(By.id("user.password")).sendKeys("1234");
		driver.findElement(By.id("user.email")).clear();
		driver.findElement(By.id("user.email")).sendKeys("javier1234@us.es");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	public void assertElements() {
		assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='add-owner-form']/div/div[2]/div/span[2]")).getText());
	}
}
