package org.springframework.samples.petclinic.UI;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AñadirComentarioPaseoUITest {

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
	public void testAñadirComentarioPaseoUI() throws Exception {
		driver.get("http://localhost:" + port);

		loginOwner();

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

	public void loginOwner() throws Exception {
		driver.get("http://localhost:" + port);
		driver.findElement(By.linkText("LOGIN")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("owner1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("0wn3r");
		driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
		assertEquals("OWNER1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
	}

	public void completeForm() throws Exception {
		driver.findElement(By.id("WalkId")).click();
		driver.findElement(By.xpath("(//img[@id='walkMap'])[2]")).click();

		driver.findElement(By.id("title")).click();
		driver.findElement(By.id("title")).clear();
		driver.findElement(By.id("title")).sendKeys("Titulo de Prueba");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Esto es una descripcion para esta prueba");
		new Select(driver.findElement(By.id("rating"))).selectByVisibleText("5");
		driver.findElement(By.xpath("//option[@value='5']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		comentarios = driver.findElement(By.id("tablaComentarios")).findElements(By.id("comentarioFila")).size();
	}

	public void assertElements() throws Exception {
		assertEquals("Your comment have been submited correctly",
				driver.findElement(By.cssSelector("div.alert-success")).getText());
		assertEquals("Titulo de Prueba",
				driver.findElement(By.xpath("(//dd[@id='tituloComentario'])[" + comentarios + "]")).getText());
		assertEquals("Esto es una descripcion para esta prueba",
				driver.findElement(By.xpath("(//dd[@id='descriptionComentario'])[" + comentarios + "]")).getText());
		assertEquals("owner1",
				driver.findElement(By.xpath("(//dd[@id='nameComentario'])[" + comentarios + "]")).getText());
		assertEquals(LocalDate.now().toString(),
				driver.findElement(By.xpath("(//dd[@id='fechaComentario'])[" + comentarios + "]")).getText());
	}
}
