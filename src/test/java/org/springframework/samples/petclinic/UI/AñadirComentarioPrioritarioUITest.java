package org.springframework.samples.petclinic.UI;

import java.time.LocalDate;
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
public class AñadirComentarioPrioritarioUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	private int comentarios;

	@BeforeEach
	public void setUp() throws Exception {

		String pathToGeckoDriver = System.getenv("webdriver.gecko.driver");
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);

		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testAñadirComentarioPrioritarioUI() throws Exception {
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
		driver.findElement(By.id("ProductId")).click();
		driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Comentario UITest");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Este es un comentario para el UITest");
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		comentarios = driver.findElement(By.id("comentarios")).findElements(By.className("media-body-vet")).size();
	}

	public void assertElements() throws Exception {
		assertEquals("vet1",
				driver.findElement(By.xpath("(//strong[@id='UsernamePrioritario'])[" + comentarios + "]")).getText());
		assertEquals("Comentario UITest",
				driver.findElement(By.xpath("(//strong[@id='TituloPrioritario'])[" + comentarios + "]")).getText());
		assertEquals("Este es un comentario para el UITest",
				driver.findElement(By.xpath("(//p[@id='DescriptionPrioritario'])[" + comentarios + "]")).getText());
		assertEquals(LocalDate.now().toString(),
				driver.findElement(By.xpath("(//small[@id='FechaPrioritario'])[" + comentarios + "]")).getText());
	}
}
