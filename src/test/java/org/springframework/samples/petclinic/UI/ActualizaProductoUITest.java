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
public class ActualizaProductoUITest {
  
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
  public void testActualizaProductoUI() throws Exception {
    driver.get("http://localhost:"+port);
    driver.findElement(By.linkText("LOGIN")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("admin1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("4dm1n");
    driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
    assertEquals("ADMIN1", driver.findElement(By.xpath("//a[@id='username']/strong")).getText().toUpperCase());
    
    driver.findElement(By.id("ProductId")).click();
    driver.findElement(By.xpath("//div[@id='infoProducto']/a/img")).click();
    driver.findElement(By.xpath("//a[contains(text(),'Edit Product')]")).click();
    
    driver.findElement(By.id("name")).clear();
    driver.findElement(By.id("name")).sendKeys("Prueba UITest");
    driver.findElement(By.id("description")).clear();
    driver.findElement(By.id("description")).sendKeys("Esto es una prueba para los UITest, en concreto el caso positivo");
    driver.findElement(By.name("stock")).clear();
    driver.findElement(By.name("stock")).sendKeys("20");
    driver.findElement(By.id("urlImage")).clear();
    driver.findElement(By.id("urlImage")).sendKeys("https://images.app.goo.gl/bZnZ3fRGskVLhWf79");
    driver.findElement(By.name("unitPrice")).clear();
    driver.findElement(By.name("unitPrice")).sendKeys("20.0");
    new Select(driver.findElement(By.id("category"))).selectByVisibleText("ACCESORY");
    driver.findElement(By.xpath("//option[@value='ACCESORY']")).click();
    driver.findElement(By.xpath("//button[@type='submit']")).click();
    
    
    assertEquals("https://images.app.goo.gl/bZnZ3fRGskVLhWf79", driver.findElement(By.xpath("(//img[@id='imgProducto'])")).getAttribute("src") );
    assertEquals("Prueba UITest", driver.findElement(By.id("NombreProducto")).getText());
    assertEquals("Esto es una prueba para los UITest, en concreto el caso positivo", driver.findElement(By.id("DescripcionProducto")).getText());
    assertEquals("20.0", driver.findElement(By.id("PrecioProducto")).getText());
    assertEquals("20", driver.findElement(By.id("stockProducto")).getText());
    assertEquals("ACCESORY", driver.findElement(By.id("categoryProducto")).getText());
    assertEquals("Yes", driver.findElement(By.id("avaliableProducto")).getText());
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