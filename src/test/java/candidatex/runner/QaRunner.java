package candidatex.runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class) @CucumberOptions(features = "src/test/java/candidatex/features", glue = {
    "candidatex/stepDefinitions"}, tags = "@regression", monochrome = true, plugin = {"pretty",
    "json:target/Cucumber.json", "html:target/cucumber-reports/Cucumber.html"})
public class QaRunner {
}
