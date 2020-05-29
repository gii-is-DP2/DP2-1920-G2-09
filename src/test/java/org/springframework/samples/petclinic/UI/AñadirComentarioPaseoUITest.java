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
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AñadirComentarioPaseoUITest {

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
	void testAñadirComentarioPaseoUI() throws Exception {
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
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.linkText("LOGIN")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("owner1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("0wn3r");
		this.driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		Assert.assertEquals("OWNER1",
				this.driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		this.driver.findElement(By.id("WalkId")).click();
		this.driver.findElement(By.xpath("(//img[@id='walkMap'])[2]")).click();

		this.driver.findElement(By.id("title")).click();
		this.driver.findElement(By.id("title")).clear();
		this.driver.findElement(By.id("title")).sendKeys("Titulo de Prueba");
		this.driver.findElement(By.id("description")).clear();
		this.driver.findElement(By.id("description")).sendKeys("Esto es una descripcion para esta prueba");
		new Select(this.driver.findElement(By.id("rating"))).selectByVisibleText("5");
		this.driver.findElement(By.xpath("//option[@value='5']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		this.comentarios = this.driver.findElement(By.id("tablaComentarios")).findElements(By.id("comentarioFila"))
				.size();
	}

	public void assertElements() throws Exception {
		Assert.assertEquals("Your comment have been submited correctly",
				this.driver.findElement(By.cssSelector("div.alert-success")).getText());
		Assert.assertEquals("Titulo de Prueba", this.driver
				.findElement(By.xpath("(//dd[@id='tituloComentario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals("Esto es una descripcion para esta prueba", this.driver
				.findElement(By.xpath("(//dd[@id='descriptionComentario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals("owner1",
				this.driver.findElement(By.xpath("(//dd[@id='nameComentario'])[" + this.comentarios + "]")).getText());
		Assert.assertEquals(LocalDate.now().toString(),
				this.driver.findElement(By.xpath("(//dd[@id='fechaComentario'])[" + this.comentarios + "]")).getText());
	}
}
