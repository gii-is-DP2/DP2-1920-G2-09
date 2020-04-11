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
public class ActualizarPaseoUITest {
  
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
  public void testActualizarPaseoUI() throws Exception {
    driver.get("http://localhost:"+port);
    driver.findElement(By.linkText("LOGIN")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("admin1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("4dm1n");
    driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
    assertEquals("ADMIN1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
    
    driver.findElement(By.id("WalkId")).click();
    driver.findElement(By.xpath("//img[@id='walkMap']")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Edit Walk')]")).click();
    
    driver.findElement(By.name("name")).clear();
    driver.findElement(By.name("name")).sendKeys("Prueba UITest");
    driver.findElement(By.name("description")).clear();
    driver.findElement(By.name("description")).sendKeys("Esto es una prueba para los UITest, en concreto el caso positivo");
    driver.findElement(By.name("map")).clear();
    driver.findElement(By.name("map")).sendKeys("https://images.app.goo.gl/y6xogjo3eKaDjwjs8");
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    
    assertEquals("https://images.app.goo.gl/y6xogjo3eKaDjwjs8", driver.findElement(By.xpath("(//img[@id='walkMap'])")).getAttribute("src") );
    assertEquals("Prueba UITest", driver.findElement((By.xpath("(//td[@id='walkName'])"))).getText());
    assertEquals("Esto es una prueba para los UITest, en concreto el caso positivo",
    		driver.findElement(By.xpath("(//td[@id='walkDescription'])")).getText());
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



