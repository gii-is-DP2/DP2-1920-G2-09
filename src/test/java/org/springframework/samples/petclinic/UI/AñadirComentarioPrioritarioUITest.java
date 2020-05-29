package org.springframework.samples.petclinic.UI;

import java.time.LocalDate;
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
class AñadirComentarioPrioritarioUITest {

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
	void testAñadirComentarioPrioritarioUI() throws Exception {
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
		this.driver.findElement(By.id("ProductId")).click();
		this.driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();
		this.driver.findElement(By.id("title")).clear();
		this.driver.findElement(By.id("title")).sendKeys("Comentario UITest");
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("Este es un comentario para el UITest");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.comentarios = this.driver.findElement(By.id("comentarios")).findElements(By.className("media-body-vet"))
				.size();
	}

	public void assertElements() throws Exception {
		Assert.assertEquals("vet1", this.driver
				.findElement(By.xpath("(//strong[@id='UsernamePrioritario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals("Comentario UITest", this.driver
				.findElement(By.xpath("(//strong[@id='TituloPrioritario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals("Este es un comentario para el UITest", this.driver
				.findElement(By.xpath("(//p[@id='DescriptionPrioritario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals(LocalDate.now().toString(), this.driver
				.findElement(By.xpath("(//small[@id='FechaPrioritario'])[" + this.comentarios + "]")).getText());
	}
}
