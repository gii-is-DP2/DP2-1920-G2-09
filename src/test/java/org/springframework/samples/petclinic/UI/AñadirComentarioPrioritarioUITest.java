package org.springframework.samples.petclinic.UI;


import java.util.regex.Pattern;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AñadirComentarioPrioritarioUITest {
 
	@LocalServerPort
	private int port;
	
	private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();

  @BeforeEach
  public void setUp() throws Exception {
   
	  String pathToGeckoDriver= System.getenv("webdriver.gecko.driver");
	  System.setProperty("webdriver.gecko.driver", pathToGeckoDriver);
	  
	  driver = new FirefoxDriver();
    baseUrl = "https://www.google.com/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
  }

  @Test
  public void testAñadirComentarioPrioritarioUI() throws Exception {
    driver.get("http://localhost:"+port);
    driver.findElement(By.linkText("LOGIN")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("vet1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("v3t");
    driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
    assertEquals("VET1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
    
    driver.findElement(By.id("ProductId")).click();
    driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();
    driver.findElement(By.id("title")).clear();
    driver.findElement(By.id("title")).sendKeys("Comentario UITest");
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Este es un comentario para el UITest");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    
    int comentarios = driver.findElement(By.id("comentarios")).findElements(By.className("media-body-vet")).size();
    assertEquals("vet1", driver.findElement(By.xpath("(//strong[@id='UsernamePrioritario'])["+comentarios+"]")).getText());
    assertEquals("Comentario UITest", driver.findElement(By.xpath("(//strong[@id='TituloPrioritario'])["+comentarios+"]")).getText());
    assertEquals("Este es un comentario para el UITest", driver.findElement(By.xpath("(//p[@id='DescriptionPrioritario'])["+comentarios+"]")).getText());
    assertEquals(LocalDate.now().toString(),driver.findElement(By.xpath("(//small[@id='FechaPrioritario'])["+comentarios+"]")).getText());
  }

  @AfterEach
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }

  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
