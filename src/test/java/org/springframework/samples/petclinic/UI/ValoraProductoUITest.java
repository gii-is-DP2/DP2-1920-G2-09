package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValoraProductoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	private Double valoracionFinal;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Disabled
	@Test
	void testValoraProductoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);

		this.loggingOwner();

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

	public void loggingOwner() {
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("0wn3r");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	}

	public void completeForm() {
		this.driver.findElement(By.id("ProductId")).click();

		this.driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();

		Double valoracionInicial = Double
				.valueOf(this.driver.findElement(By.id("RatingProducto")).getText().replace(",", "."));
		Integer valoracion = 4;
		new Select(this.driver.findElement(By.id("rating"))).selectByVisibleText(String.valueOf(valoracion));
		this.valoracionFinal = (valoracionInicial + valoracion) / 2;
		this.driver.findElement(By.xpath("//option[@value='4']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void assertElements() {
		Assert.assertEquals(this.valoracionFinal,
				Double.valueOf(this.driver.findElement(By.id("RatingProducto")).getText().replace(",", ".")));
	}
}
