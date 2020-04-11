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
public class RegistroUsuarioUITest {
 
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

  @Disabled
  @Test
  public void testRegistroUsuarioUI() throws Exception {
	  driver.get("http://localhost:"+port);
	    driver.findElement(By.linkText("REGISTER")).click();
	    driver.findElement(By.id("firstName")).clear();
	    driver.findElement(By.id("firstName")).sendKeys("Javier");
	    driver.findElement(By.id("lastName")).clear();
	    driver.findElement(By.id("lastName")).sendKeys("Apellido muy largoo");
	    driver.findElement(By.id("address")).clear();
	    driver.findElement(By.id("address")).sendKeys("Reina Mercedes 24");
	    driver.findElement(By.id("city")).clear();
	    driver.findElement(By.id("city")).sendKeys("Sevilla");
	    driver.findElement(By.id("telephone")).clear();
	    driver.findElement(By.id("telephone")).sendKeys("11122233");
	    driver.findElement(By.id("user.username")).clear();
	    driver.findElement(By.id("user.username")).sendKeys("javier991");
	    driver.findElement(By.id("user.password")).clear();
	    driver.findElement(By.id("user.password")).sendKeys("1234");
	    driver.findElement(By.id("user.email")).clear();
	    driver.findElement(By.id("user.email")).sendKeys("javier1234@us.es");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    
	    driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("javier991");
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("1234");
	    driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
	    
	    driver.findElement(By.xpath("//a[@id='username']/span[2]")).click();
	    driver.findElement(By.xpath("//a[contains(@href, '/owners/profile')]")).click();
	    
	    assertEquals("Javier Apellido muy largoo", driver.findElement(By.xpath("//td[@id='nombreUser']/b")).getText());
	    assertEquals("Reina Mercedes 24", driver.findElement(By.id("direccionUser")).getText());
	    assertEquals("Sevilla", driver.findElement(By.id("ciudadUser")).getText());
	    assertEquals("11122233", driver.findElement(By.id("telefonoUser")).getText());
	    assertEquals("javier1234@us.es", driver.findElement(By.id("emailUser")).getText());
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


