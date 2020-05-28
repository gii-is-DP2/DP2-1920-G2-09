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
class AñadirACarritoNegativoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private Integer stock;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();

		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	void testAñadirACarritoNegativoUI() throws Exception {

		this.driver.get("http://localhost:" + this.port);

		this.loginOwner();

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

	public void loginOwner() throws Exception {
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("0wn3r");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("OWNER1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
		this.driver.findElement(By.id("ProductId")).click();
	}

	public void completeForm() throws Exception {

		this.driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();

		this.stock = new Integer(this.driver.findElement(By.id("stockProducto")).getText());

		this.driver.findElement(By.xpath("//input[@name='quantity']")).clear();
		this.driver.findElement(By.xpath("//input[@name='quantity']")).sendKeys("210");
		this.driver.findElement(By.xpath("//form[@id='add-product-to-shoppingCart']/button")).click();
	}

	public void assertElements() throws Exception {
		Assert.assertEquals(String.valueOf(this.stock), this.driver.findElement(By.id("stockProducto")).getText());
		Assert.assertEquals("The quantity selected is greather than the stock",
				this.driver.findElement(By.xpath("//body/div/div/div[2]")).getText());
	}
}
