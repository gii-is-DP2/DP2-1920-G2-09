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
class AñadirPaseoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int walks;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		this.driver = new FirefoxDriver();
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	void testAñadirPaseoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);

		this.loginAdmin();

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

	public void loginAdmin() throws Exception {
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("admin1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("4dm1n");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("ADMIN1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		this.driver.findElement(By.id("AdminId")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Añadir Paseo')]")).click();
		this.driver.findElement(By.name("name")).clear();
		this.driver.findElement(By.name("name")).sendKeys("Prueba UITest");
		this.driver.findElement(By.name("description")).clear();
		this.driver.findElement(By.name("description"))
				.sendKeys("Esto es una prueba para los UITest, en concreto el caso positivo");
		this.driver.findElement(By.name("map")).clear();
		this.driver.findElement(By.name("map")).sendKeys("https://images.app.goo.gl/y6xogjo3eKaDjwjs8");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.walks = this.driver.findElement(By.id("walksImg")).findElements(By.tagName("a")).size();
	}

	public void assertElements() throws Exception {
		Assert.assertEquals("https://images.app.goo.gl/y6xogjo3eKaDjwjs8",
				this.driver.findElement(By.xpath("(//img[@id='walkMap'])[" + this.walks + "]")).getAttribute("src"));
		Assert.assertEquals("Prueba UITest",
				this.driver.findElement(By.xpath("(//div[@id='walkName'])[" + this.walks + "]")).getText());
		Assert.assertEquals("Esto es una prueba para los UITest, en concreto el caso positivo",
				this.driver.findElement(By.xpath("(//div[@id='walkDescription'])[" + this.walks + "]")).getText());
	}
}
