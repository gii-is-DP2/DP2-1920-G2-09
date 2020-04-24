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
public class RegistroUsuarioUITest {

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

	@Disabled
	@Test
	public void testRegistroUsuarioUI() throws Exception {
		driver.get("http://localhost:" + port);
		
		completeForm();
		
		logging();
		
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
		driver.findElement(By.id("lastName")).sendKeys("Apellido muy largoo");
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
	
	public void logging() {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("javier991");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("1234");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	}
	
	public void assertElements() {
		driver.findElement(By.xpath("//a[@id='username']/span[2]")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/owners/profile')]")).click();
		
		assertEquals("Javier Apellido muy largoo", driver.findElement(By.xpath("//td[@id='nombreUser']/b")).getText());
		assertEquals("Reina Mercedes 24", driver.findElement(By.id("direccionUser")).getText());
		assertEquals("Sevilla", driver.findElement(By.id("ciudadUser")).getText());
		assertEquals("11122233", driver.findElement(By.id("telefonoUser")).getText());
		assertEquals("javier1234@us.es", driver.findElement(By.id("emailUser")).getText());
	}
}
