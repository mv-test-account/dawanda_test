package dawanda_tests;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;

import org.testng.annotations.BeforeMethod;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import javax.jws.WebResult;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;

public class dawanda_login {
	public WebDriver driver;
	public String username;
	public Boolean registrationSuccesfull = false;

	@Test
	public void openRegistrationPage() throws Exception {
		long random_n = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;

		displayElement(By.className("header-user-toggle"));

		waitOnButton(By.cssSelector("a[href*='register']"));

		waitOnElement(By.id("firstname"));
		username = "frontend-test-user-" + random_n;
		String email = "frontend-tests+-" + random_n + "@dawandamail.com";
		driver.findElement(By.id("firstname")).sendKeys(username);
		driver.findElement(By.id("lastname")).sendKeys(username);
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("email")).sendKeys(email);
		driver.findElement(By.id("password")).sendKeys("Password1");
		driver.findElement(By.id("accept_privacy")).click();


		waitOnButton(By.id("register_submit"));
		
		waitOnElement(By.className(("title")));
		System.out.println(driver.findElement(By.className(("title"))).getText()); 
		
		assertTrue(driver.findElement(By.className(("title"))).getText().contains("ALMOST THERE!"));
		registrationSuccesfull = true;
		displayElement(By.className("header-user-toggle"));
		driver.findElement(By.cssSelector("a[href='/account/logout']")).click();

		// <h1 class="title">
		// Almost there!
		// </h1>

	}

	@Test
	public void loginTest() throws Exception {
		
		displayElement(By.className("header-user-toggle"));

		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.findElement(By.cssSelector("a[href*='login']")).click();

		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.id("login_credentials_password")).sendKeys("Password1");

		if (driver.findElement(By.id("remember_me_checkbox")).isEnabled())
			driver.findElement(By.id("remember_me_checkbox")).click();
		driver.findElement(By.id("login_submit")).click();
		System.out.println(1);

		WebDriverWait wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.className("header-shopping-bag-icon"))));
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

		WebElement header = driver.findElement(By.className("header-user-toggle"));
		System.out.println(header.getText());
		assertTrue(header.getText().contains("Michal71"));

		displayElement(By.className("header-user-toggle"));

		driver.findElement(By.cssSelector("a[href='/account/logout']")).click();
		System.out.println(1);
		wait = new WebDriverWait(driver, 10);
		wait.until(ExpectedConditions.visibilityOfElementLocated((By.id("goodbye"))));
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);

	}

	public void waitOnElement(By by) {
		WebDriverWait wait = new WebDriverWait(driver, 20);
		wait.until(ExpectedConditions.visibilityOfElementLocated((by)));
	}

	public void displayElement(By by) throws Exception {
		Actions action = new Actions(driver);
		WebElement we = driver.findElement(by);
		action.moveToElement(we).build().perform();
	}

	public void getscreenshot(String name) throws Exception {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		String destDir = System.getProperty("user.dir") + "/" + "test-output/screenshots";
		new File(destDir).mkdirs();
		String destFile = destDir + "/" + name
				+ new SimpleDateFormat("MM-dd-yyyy_HH-ss").format(new GregorianCalendar().getTime()) + ".png";

		FileUtils.copyFile(scrFile, new File(destFile));

	}
	
	
	public void waitOnButton(By by) throws Exception{
		try {
			WebElement registerButton = driver.findElement(by);
			for(int i = 0; i<3; i++){
				if (registerButton.isDisplayed())
					registerButton.click();
			}
		} catch (Exception e) {
			System.out.println(e);
			getscreenshot("button");
		}
	}

	@BeforeMethod
	public void beforeMethod() {
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.get("http://en.dawanda.com/");
		System.out.println(driver.getTitle());
	}

	@AfterMethod
	public void afterMethod() {
		driver.quit();
	}

}
