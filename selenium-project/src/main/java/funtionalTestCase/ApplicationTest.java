package funtionalTestCase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;


public class ApplicationTest {
	public static void main(String[] args) {

		// Set up WebDriver
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver(options);

		try {

			// Navigate to the URL
			driver.get("https://demo.dealsdray.com/");

			// Add an explicit wait to ensure the element is available
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

			// Locate the email field, password field, and login button
			WebElement emailField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
			WebElement passwordField = driver.findElement(By.name("password"));
			WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

			// Enter credentials and log in
			emailField.sendKeys("prexo.mis@dealsdray.com");
			passwordField.sendKeys("prexo.mis@dealsdray.com");
			loginButton.click();

			WebElement specificButton = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//button[contains(@class, 'MuiButtonBase-root') and contains(@class, 'css-46up3a')]")));
			specificButton.click();

			WebElement specificElement = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("/html/body/div/div/div[1]/div/div[2]/div[1]/div[2]/div/div[1]")));
			specificElement.click();

			WebElement addBulkOrdersButton = wait.until(
					ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Add Bulk Orders')]")));
			addBulkOrdersButton.click();

			WebElement fileInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("/html/body/div/div/div[2]/div[2]/div/div/div[2]/div[3]/div/div/input")));
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileInput);
			fileInput.sendKeys("D:\\demo-data.xlsx");

			WebElement targetButton = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div[2]/div/div/div[2]/div[3]/button")));
			targetButton.click();

			WebElement validate = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("/html/body/div/div/div[2]/div[2]/div/div/div[2]/div[3]/button")));
			validate.click();

			wait.until(ExpectedConditions.alertIsPresent());
			Alert alert = driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();

			Actions actions = new Actions(driver);
			actions.sendKeys(Keys.PAGE_DOWN).perform();
			actions.sendKeys(Keys.PAGE_DOWN).perform();

			WebElement maxview = wait.until(ExpectedConditions
					.elementToBeClickable(By.xpath("/html/body/div/div/div/div[1]/div/div[1]/button/span[1]")));
			maxview.click();

			try {

				String folderPath = "screenshotApplicationTest/";
				File dir = new File(folderPath);
				if (!dir.exists())
					dir.mkdirs();

				// Setting the timestamp format for the screenshot name
				String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
				String screenshotPath = folderPath + "/finalScreenshot-" + timeStamp + ".png";

				// Take Screenshot
				File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				FileUtils.copyFile(screenshot, new File(screenshotPath));

				System.out.println("Screenshot taken: " + screenshotPath);

			} catch (IOException e) {
				System.out.println("Error taking screenshot: " + e.getMessage());
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Clean up and close the driver
			driver.quit();
		}
	}

}
