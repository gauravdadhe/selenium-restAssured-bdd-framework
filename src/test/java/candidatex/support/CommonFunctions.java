package candidatex.support;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonFunctions {

    public static void waitForPageLoad(WebDriver driver) {
        try {
            ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driver) {
                    return (
                        ((JavascriptExecutor) driver).executeScript("return document.readyState")
                            .toString().equals("complete")
                            && (Boolean) ((JavascriptExecutor) driver)
                            .executeScript("return jQuery.active == 0"));
                }
            };

            WebDriverWait wait = new WebDriverWait(driver, Integer
                .parseInt(TestProperties.loadProperty().getProperty("browser.ExplicitDrvierWait")));
            wait.until(expectation);
            Thread.sleep(5000);
        } catch (Exception e) {

        }
    }
}
