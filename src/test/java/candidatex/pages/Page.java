package candidatex.pages;

import candidatex.browser.Browser;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;


public class Page {
    public WebDriver driver = Browser.getBrowserInstance();
    public ConduitPage conduitPage = PageFactory.initElements(driver, ConduitPage.class);
}
