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
class ComentaProductoNegativoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	private int comentarios;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	void testComentaProductoNegativoUI() throws Exception {
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
		Assert.assertEquals("OWNER1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() {
		this.driver.findElement(By.id("ProductId")).click();
		this.driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();

		this.comentarios = this.driver.findElement(By.id("comentarios")).findElements(By.className("media-body"))
				.size();
		this.driver.findElement(By.id("title")).clear();
		this.driver.findElement(By.id("title")).sendKeys("UITest");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void assertElements() {
		Assert.assertEquals(this.comentarios,
				this.driver.findElement(By.id("comentarios")).findElements(By.className("media-body")).size());
	}

}
