package candidatex.stepDefinitions;

import candidatex.browser.Browser;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {
    private WebDriver driver;

    @After("not @api") public void afterTest(Scenario scenario) {
        if (Browser.driver != null) {
            if (scenario.isFailed()) {
                captureScreenShot(scenario);
            }
//            Browser.closeBrowser();
        }
    }

    public void captureScreenShot(Scenario myScenario) {
        try {
            driver = Browser.getBrowserInstance();
            myScenario.write("Current Page URL is " + driver.getCurrentUrl());
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            myScenario.embed(screenshot, "image/png");
        } catch (Exception e) {
            BasicConfigurator.configure();
            e.printStackTrace();
        }

    }
}
