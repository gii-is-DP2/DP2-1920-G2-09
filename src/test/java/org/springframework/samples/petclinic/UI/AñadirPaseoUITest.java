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
public class AñadirPaseoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int walks;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAñadirPaseoUI() throws Exception {
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
		driver.findElement(By.xpath("//a[contains(text(),'Añadir Paseo')]")).click();
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Prueba UITest");
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description"))
				.sendKeys("Esto es una prueba para los UITest, en concreto el caso positivo");
		driver.findElement(By.name("map")).clear();
		driver.findElement(By.name("map")).sendKeys("https://images.app.goo.gl/y6xogjo3eKaDjwjs8");
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		walks = driver.findElement(By.id("walksImg")).findElements(By.tagName("a")).size();
	}

	public void assertElements() throws Exception {
		assertEquals("https://images.app.goo.gl/y6xogjo3eKaDjwjs8",
				driver.findElement(By.xpath("(//img[@id='walkMap'])[" + walks + "]")).getAttribute("src"));
		assertEquals("Prueba UITest",
				driver.findElement((By.xpath("(//div[@id='walkName'])[" + walks + "]"))).getText());
		assertEquals("Esto es una prueba para los UITest, en concreto el caso positivo",
				driver.findElement(By.xpath("(//div[@id='walkDescription'])[" + walks + "]")).getText());
	}
}
