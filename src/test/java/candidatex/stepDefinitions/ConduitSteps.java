package candidatex.stepDefinitions;

import candidatex.pages.Page;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

public class ConduitSteps {
    Page page = new Page();

    @Given("I login to Conduit") public void iLoginToConduit() {
        page.conduitPage.launchConduit();
    }

    @When("I click on {string} from top navigation bar")
    public void iClickOnFromTopNavigationBar(String linkName) {
        switch (linkName.trim().toUpperCase()) {
            case "HOME":
                page.conduitPage.clickHomeNavigationLink();
                break;
            case "SIGN IN":
                page.conduitPage.clickSignInNavigationLink();
                break;
            case "SIGN UP":
                page.conduitPage.clickSignUpNavigationLink();
                break;
            default:
                Assert.fail("Top navigation " + linkName + " is not defined.");
        }
    }

    @And("I enter credentials as user {string} and password {string}")
    public void iEnterCredentialsAsUserAndPassword(String email, String password) {
        page.conduitPage.setEmailInput(email);
        page.conduitPage.setPasswordInput(password);
    }

    @When("I click on 'Sign in' button") public void iClickOnSignInButton() {
        page.conduitPage.clickSignInButton();
    }

    @Then("I verify error message 'email or password is invalid' is displayed")
    public void iVerifyErrorMessageIsDisplayed() {
        Assert.assertTrue("Error message is not displayed.",
            page.conduitPage.invalidSignInMessage.isDisplayed());
    }

    @When("I click on first popular tag") public void iClickOnFirstPopularTag() {
        page.conduitPage.clickFirstPopularTag();
    }

    @Given("I capture the first popular tag") public void iCaptureTheFirstPopularTag() {
        page.conduitPage.captureFirstPopularTag();
    }

    @Then("I verify navigation for the selected tag is opened")
    public void iVerifyNavigationForTheSelectedTagIsOpened() {
        Assert.assertTrue("Popular tag nav is not displayed.",
            page.conduitPage.isPopularTagNavigationDisplayed());
    }

    @And("I verify the navigation has feeds") public void iVerifyTheNavigationHasFeeds() {
        Assert.assertTrue("No feeds found.", page.conduitPage.articlePreviewList.size() > 0);
    }

    @When("I click on 'Global Feed' navigation") public void iClickOnGlobalFeedNavigation() {
        page.conduitPage.clickGlobalFeedNavigationLink();
    }

    @Then("I verify 'Global Feed' navigation is opened")
    public void iVerifyGlobalFeedNavigationIsOpened() {
        Assert.assertTrue("Global feeds naviagation is not opened.",
            page.conduitPage.isGlobalFeedNavigationDisplayed());
    }

    @Then("I verify page link {string} is selected")
    public void iVerifyPageLinkIsSelected(String expectedPageNumber) throws InterruptedException {
        Assert.assertTrue("Page " + expectedPageNumber + " is not opened.",
            page.conduitPage.isPageNumberOpened(expectedPageNumber));
    }

    @When("I click on page {string}") public void iClickOnPage(String pageNumber) {
        page.conduitPage.navigateToPage(pageNumber);
    }
}
