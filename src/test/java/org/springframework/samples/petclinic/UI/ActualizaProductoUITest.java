package org.springframework.samples.petclinic.UI;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActualizaProductoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testActualizaProductoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("admin1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("4dm1n");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("ADMIN1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());

		this.driver.findElement(By.id("ProductId")).click();
		this.driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Edit Product')]")).click();

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
		this.driver.findElement(By.name("unitPrice")).sendKeys("20.0");
		new Select(this.driver.findElement(By.id("category"))).selectByVisibleText("ACCESORY");
		this.driver.findElement(By.xpath("//option[@value='ACCESORY']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		Assert.assertEquals("https://images.app.goo.gl/bZnZ3fRGskVLhWf79",
				this.driver.findElement(By.xpath("(//img[@id='imgProducto'])")).getAttribute("src"));
		Assert.assertEquals("Prueba UITest", this.driver.findElement(By.id("NombreProducto")).getText());
		Assert.assertEquals("Esto es una prueba para los UITest, en concreto el caso positivo",
				this.driver.findElement(By.id("DescripcionProducto")).getText());
		Assert.assertEquals("20.0", this.driver.findElement(By.id("PrecioProducto")).getText());
		Assert.assertEquals("20", this.driver.findElement(By.id("stockProducto")).getText());
		Assert.assertEquals("ACCESORY", this.driver.findElement(By.id("categoryProducto")).getText());
		Assert.assertEquals("Yes", this.driver.findElement(By.id("avaliableProducto")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}