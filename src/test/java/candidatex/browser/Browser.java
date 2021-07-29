package candidatex.browser;

import candidatex.support.TestProperties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Browser {
    private Browser() {
    }

    public static RemoteWebDriver driver;
    public static String binaryURL =
        TestProperties.loadProperty().getProperty("driver.binaries.path");
    public static String browserBit = TestProperties.loadProperty().getProperty("browserBit");
    public static String operatingSystem =
        TestProperties.loadProperty().getProperty("operatingSystem");
    public static String browserName = TestProperties.loadProperty().getProperty("browserName");
    public static int globalTimeout =
        Integer.parseInt(TestProperties.loadProperty().getProperty("browser.ExplicitDriverWait"));

    public static void initBrowser() {
        try {
            String executable = browserName;
            if (operatingSystem.equalsIgnoreCase("windows")) {
                executable = executable + ".exe";
            }
            System.setProperty("webdriver.chrome.driver",
                binaryURL + "/" + operatingSystem + "/" + browserBit + "/" + executable);
            ChromeOptions options = new ChromeOptions();
            //options.addArguments("headless");
            driver = new ChromeDriver(options);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        driver.manage().timeouts().implicitlyWait(globalTimeout, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    public static WebDriver getBrowserInstance() {
        if (driver == null) {
            initBrowser();
        }
        return driver;
    }

    public static void closeBrowser() {
        driver.quit();
    }
}
