import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ResolutionTesting {

    // Define Resolutions and Devices
    private static final Map<String, Dimension> resolutions = new HashMap<>();

    static {
        resolutions.put("Desktop-1920x1080", new Dimension(1920, 1080));
        resolutions.put("Desktop-1366x768", new Dimension(1366, 768));
        resolutions.put("Desktop-1536x864", new Dimension(1536, 864));
        resolutions.put("Mobile-360x640", new Dimension(360, 640));
        resolutions.put("Mobile-414x896", new Dimension(414, 896));
        resolutions.put("Mobile-375x667", new Dimension(375, 667));
    }

    // Define Browsers
    private static final String[] browsers = {"chrome", "firefox", "safari"};

    // Define URLs
    private static final String[] urls = {
        "https://www.getcalley.com/",
        "https://www.getcalley.com/calley-lifetime-offer/",
        "https://www.getcalley.com/see-a-demo/",
        "https://www.getcalley.com/calley-teams-features/",
        "https://www.getcalley.com/calley-pro-features/",
        "https://www.getcalley.com/calley-personal-features/"
    };

    public static void main(String[] args) {
        for (String browser : browsers) {
            WebDriver driver = getDriver(browser);
            if (driver == null) continue;

            for (Map.Entry<String, Dimension> entry : resolutions.entrySet()) {
                String deviceResolution = entry.getKey();
                Dimension size = entry.getValue();

                for (String url : urls) {
                    takeScreenshot(driver, url, browser, deviceResolution, size);
                }
            }
            driver.quit();
        }
    }

    private static WebDriver getDriver(String browser) {
        switch (browser) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver();
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            case "safari":
                // Skip Safari if not on macOS
                if (!System.getProperty("os.name").contains("Mac")) {
                    System.out.println("Safari is only supported on macOS. Skipping Safari tests.");
                    return null;
                }
                return new SafariDriver();
            default:
                System.out.println("Unsupported browser: " + browser);
                return null;
        }
    }

    private static void takeScreenshot(WebDriver driver, String url, String browser, String deviceResolution, Dimension size) {
        try {
            driver.manage().window().setSize(size);
            driver.get(url);
            Thread.sleep(2000);  // Allow time for the page to load fully

            // Creating the screenshot directory structure
            String folderPath = "screenshots/" + browser + "/" + deviceResolution;
            File dir = new File(folderPath);
            if (!dir.exists()) dir.mkdirs();

            // Setting the timestamp format for the screenshot name
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String screenshotPath = folderPath + "/Screenshot-" + timeStamp + ".png";

            // Take Screenshot
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(screenshotPath));

            System.out.println("Screenshot taken: " + screenshotPath);

        } catch (InterruptedException | IOException e) {
            System.out.println("Error taking screenshot: " + e.getMessage());
        }
    }
}
