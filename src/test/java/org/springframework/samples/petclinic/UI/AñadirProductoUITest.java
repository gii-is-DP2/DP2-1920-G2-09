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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AñadirProductoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	private int productos;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	void testAñadirProductoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);

		this.loggingAdmin();

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

	public void loggingAdmin() {
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("admin1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("4dm1n");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("ADMIN1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() {
		this.driver.findElement(By.id("ProductId")).click();
		this.productos = this.driver.findElement(By.id("productList")).findElements(By.tagName("a")).size();
		this.driver.findElement(By.id("AdminId")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Añadir Producto')]")).click();
		this.driver.findElement(By.id("name")).clear();
		this.driver.findElement(By.id("name")).sendKeys("Prueba UITest");
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description"))
				.sendKeys("Esto es una prueba para los UITest, en concreto el caso positivo");
		this.driver.findElement(By.name("stock")).clear();
		this.driver.findElement(By.name("stock")).sendKeys("20");
		this.driver.findElement(By.id("urlImage")).clear();
		this.driver.findElement(By.id("urlImage")).sendKeys("https://images.app.goo.gl/bZnZ3fRGskVLhWf79");
		this.driver.findElement(By.name("unitPrice")).clear();
		this.driver.findElement(By.name("unitPrice")).sendKeys("20");
		new Select(this.driver.findElement(By.id("category"))).selectByVisibleText("HYGIENE");
		this.driver.findElement(By.xpath("//option[@value='HYGIENE']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void assertElements() {
		Assert.assertEquals(this.productos + 1,
				this.driver.findElement(By.id("productList")).findElements(By.tagName("a")).size());
	}
}
