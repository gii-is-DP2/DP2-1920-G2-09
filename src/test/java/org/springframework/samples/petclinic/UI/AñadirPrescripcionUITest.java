package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AñadirPrescripcionUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int pos;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	void testAñadirPrescripcionUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);

		this.loginVet();

		this.completeForm();

		this.assertElements();

	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	public void loginVet() throws Exception {
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("vet1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("v3t");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("VET1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		this.driver.findElement(By.id("OwnersId")).click();
		this.driver.findElement(By.id("botonBusqueda")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Alejandro Blanco')]")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/owners/1/pets/1/prescriptions/new')]")).click();

		this.driver.findElement(By.name("dateInicio")).clear();
		this.driver.findElement(By.name("dateInicio")).sendKeys("2022/02/16");
		this.driver.findElement(By.name("dateFinal")).clear();
		this.driver.findElement(By.name("dateFinal")).sendKeys("2022/02/20");
		this.driver.findElement(By.name("name")).clear();
		this.driver.findElement(By.name("name")).sendKeys("Titulo  para UITest");
		this.driver.findElement(By.name("description")).clear();
		this.driver.findElement(By.name("description"))
				.sendKeys("Esto es una descripción para UItest en el caso positivo");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.driver.findElement(By.linkText("Prescription List")).click();
		this.pos = this.driver.findElement(By.id("prescriptionsTable")).findElements(By.tagName("tr")).size() - 1;
	}

	public void assertElements() throws Exception {
		Assert.assertEquals("2022-02-16",
				this.driver.findElement(By.xpath("(//div[@id='Fecha'])[" + this.pos + "]")).getText());
		Assert.assertEquals("Titulo para UITest",
				this.driver.findElement(By.xpath("(//div[@id='Titulo'])[" + this.pos + "]")).getText());
		Assert.assertEquals("James Carter",
				this.driver.findElement(By.xpath("(//div[@id='Veterinario'])[" + this.pos + "]")).getText());
	}
}
