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
public class AñadirPrescripcionUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int pos;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Disabled
	@Test
	public void testAñadirPrescripcionUI() throws Exception {
		driver.get("http://localhost:" + port);

		loginVet();

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

	public void loginVet() throws Exception {
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("vet1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("v3t");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		assertEquals("VET1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		driver.findElement(By.id("OwnersId")).click();
		driver.findElement(By.id("botonBusqueda")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Alejandro Blanco')]")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/owners/1/pets/1/prescriptions/new')]")).click();

		driver.findElement(By.name("dateInicio")).clear();
		driver.findElement(By.name("dateInicio")).sendKeys("2022/02/16");
		driver.findElement(By.name("dateFinal")).clear();
		driver.findElement(By.name("dateFinal")).sendKeys("2022/02/20");
		driver.findElement(By.name("name")).clear();
		driver.findElement(By.name("name")).sendKeys("Titulo  para UITest");
		driver.findElement(By.name("description")).clear();
		driver.findElement(By.name("description")).sendKeys("Esto es una descripción para UItest en el caso positivo");
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		driver.findElement(By.linkText("Prescription List")).click();
		pos = driver.findElement(By.id("prescriptionsTable")).findElements(By.tagName("tr")).size() - 1;
	}

	public void assertElements() throws Exception {
		assertEquals("2022-02-16", driver.findElement(By.xpath("(//div[@id='Fecha'])[" + pos + "]")).getText());
		assertEquals("Titulo para UITest",
				driver.findElement(By.xpath("(//div[@id='Titulo'])[" + pos + "]")).getText());
		assertEquals("James Carter", driver.findElement(By.xpath("(//div[@id='Veterinario'])[" + pos + "]")).getText());
	}
}
