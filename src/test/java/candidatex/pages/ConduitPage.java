package candidatex.pages;

import candidatex.support.CommonFunctions;
import candidatex.support.TestProperties;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ConduitPage {
    private final WebDriver driver;
    private final String USER_NAME = "candidatex";
    private final String PASSWORD = "qa-is-cool";
    public static String firstPopularTag;

    public ConduitPage(WebDriver driver) {
        this.driver = driver;
    }

    @FindBy(how = How.XPATH, using = "//app-layout-header//ul/li/a[contains(text(),' Home ')]") public WebElement
        homeNavigationLink;

    @FindBy(how = How.XPATH, using = "//app-layout-header//ul/li/a[contains(text(),' Sign in ')]") public WebElement
        signInNavigationLink;

    @FindBy(how = How.XPATH, using = "//app-layout-header//ul/li/a[contains(text(),' Sign up ')]") public WebElement
        signUpNavigationLink;

    @FindBy(how = How.XPATH, using = "//app-auth-page//input[@placeholder='Email']") public WebElement
        emailInput;

    @FindBy(how = How.XPATH, using = "//app-auth-page//input[@placeholder='Password']") public WebElement
        passwordInput;

    @FindBy(how = How.XPATH, using = "//app-auth-page//button[contains(text(), ' Sign in ')]") public WebElement
        signInButton;

    @FindBy(how = How.XPATH, using = "//app-list-errors/ul/li[contains(text(), ' email or password is invalid ')]") public WebElement
        invalidSignInMessage;

    @FindBy(how = How.XPATH, using = "//app-home-page//div[@class='tag-list']/a") public List<WebElement>
        popularTagsList;

    @FindBy(how = How.XPATH, using = "//app-home-page//ul/li/a[contains(text(), ' Global Feed ')]") public WebElement
        globalFeedNavigationLink;

    @FindBy(how = How.XPATH, using = "//app-article-list/app-article-preview") public List<WebElement>
        articlePreviewList;

    @FindBy(how = How.XPATH, using = "//app-home-page//nav/ul[@class='pagination']/li") public List<WebElement>
        paginationList;

    @FindBy(how = How.XPATH, using = "//app-home-page//nav/ul[@class='pagination']/li[@class='page-item active']") public WebElement
        activePage;

    public void launchConduit() {
        driver.get("https://" + USER_NAME + ":" + PASSWORD + "@" + TestProperties.loadProperty()
            .getProperty("application.domainname"));
        CommonFunctions.waitForPageLoad(driver);
    }

    public void clickHomeNavigationLink() {
        homeNavigationLink.click();
    }

    public void clickSignInNavigationLink() {
        signInNavigationLink.click();
    }

    public void clickSignUpNavigationLink() {
        signUpNavigationLink.click();
    }

    public void setEmailInput(String email) {
        emailInput.clear();
        emailInput.sendKeys(email);
    }

    public void setPasswordInput(String password) {
        passwordInput.clear();
        passwordInput.sendKeys(password);
    }

    public void clickSignInButton() {
        signInButton.click();
        CommonFunctions.waitForPageLoad(driver);
    }

    public void clickFirstPopularTag() {
        popularTagsList.get(1).click();
        CommonFunctions.waitForPageLoad(driver);
    }

    public void captureFirstPopularTag() {
        firstPopularTag = popularTagsList.get(1).getText();
    }

    public void clickGlobalFeedNavigationLink() {
        globalFeedNavigationLink.click();
        CommonFunctions.waitForPageLoad(driver);
    }

    public boolean isPopularTagNavigationDisplayed() {
        WebElement popularTagNavigation = driver.findElement(
            By.xpath("//app-home-page//ul/li/a[contains(text(), '" + firstPopularTag + "')]"));
        if (popularTagNavigation.getAttribute("class").contains("active")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGlobalFeedNavigationDisplayed() {
        if (globalFeedNavigationLink.getAttribute("class").contains("active")) {
            return true;
        } else {
            return false;
        }
    }

    public void navigateToPage(String pageNumber) {
        for (WebElement thisPage : paginationList) {
            if (thisPage.findElement(By.xpath("./a")).getText().equals(pageNumber)) {
                thisPage.findElement(By.xpath("./a")).click();
                CommonFunctions.waitForPageLoad(driver);
                break;
            }
        }
    }

    public boolean isPageNumberOpened(String expectedPageNumber) throws InterruptedException {
        String actualPageNumber;
        for (int i = 0; i < 3; i++) {
            actualPageNumber = activePage.findElement(By.xpath("./a")).getText();
            if (actualPageNumber.equals(expectedPageNumber)) {
                return true;
            }
            Thread.sleep(100);
        }
        return false;
    }
}
