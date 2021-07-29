package candidatex.stepDefinitions;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;

import candidatex.support.Random;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;

public class ApiSteps {
    public static String TEST_DATA_DIR_PATH = "src/test/resources/testdata/";
    public static Response response;
    public static Response responseText;
    public static RequestSpecification requestSpecification;
    public static JSONObject msgBody = new JSONObject();
    public static Map<String, String> customHeaders = new HashMap();
    public static String username;
    public static String email;
    public static String password;

    @Given("I use {string} as base URI") public void iUseAsBaseUri(String baseURI) {
        RestAssured.baseURI = baseURI;
    }

    @When("I make {string} call to {string}")
    public void i_make_call_to(String method, String totalPath) throws InterruptedException {
        Map<String, String> queryParams = new HashMap<String, String>();

        //Get queryParams
        if (totalPath.contains("?")) {
            String[] paramPairs = ((totalPath.split("\\?"))[1]).split("&");
            for (String pair : paramPairs) {
                String[] myPair = pair.split("=");
                if (myPair.length == 1) { //When param value is blank
                    queryParams.put(myPair[0], "");
                } else {
                    queryParams.put(myPair[0], myPair[1]);
                }
            }
        }

        //Get Path
        String path = (totalPath.split("\\?"))[0];

        RestAssured.basePath = path;
        requestSpecification = RestAssured.given().headers(customHeaders).queryParams(queryParams);
        switch (method.toUpperCase().trim()) {
            case "GET":
                response = requestSpecification.given().when().get();
                break;
            case "POST":
                response = requestSpecification.body(msgBody.toJSONString()).post();
                break;
            case "DELETE":
                response = requestSpecification.given().when().delete();
                break;
            default:
                Assert.fail("Ooops!! Method [" + method + "] is not implemented.");
                break;
        }
    }

    @Given("I make REST service headers with the below fields")
    public void iMakeRestServiceHeaderWithBelowFields(DataTable headerValues)
        throws ParseException {
        customHeaders.clear();
        List<Map<String, String>> headers = headerValues.asMaps(String.class, String.class);
        Iterator myHeader = headers.iterator();

        while (myHeader.hasNext()) {
            Map<String, String> header = (Map) myHeader.next();
            customHeaders.putAll(header);
        }
    }

    @Then("I read request body from {string}") public void iReadRequestBodyFrom(String fileName)
        throws IOException, ParseException {
        msgBody.clear();

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(TEST_DATA_DIR_PATH + fileName));
        msgBody = (JSONObject) obj;
    }

    @And("I update request with below values")
    public void iUpdateRequestWithBelowValues(DataTable fieldList) {
        List<Map<String, String>> fields = fieldList.asMaps(String.class, String.class);
        for (Map<String, String> field : fields) {
            for (Map.Entry<String, String> entry : field.entrySet()) {
                msgBody.put(entry.getKey(), entry.getValue());
            }
        }
    }

    @When("I make the request empty") public void i_make_the_request_empty() {
        msgBody.clear();
    }

    @Then("I get response code {string}") public void i_get_response_code(String responseCode) {
        Assert.assertEquals(
            "Incorrect response code. Expected: " + responseCode + " Actual: " + response
                .statusCode(), Integer.parseInt(responseCode), response.statusCode());

    }

    @And("I verify response should match with contract file {string}")
    public void iVerifyResponseShouldMatchWithContractFile(String fileName) {
        String dirPath = "src/test/resources/testdata/responseContracts/" + fileName + ".json";
        response.then().assertThat().body(matchesJsonSchema(new File(dirPath)));
    }


    @Given("I reset API configuration") public void iResetAPIParameters() {
        response = null;
        responseText = null;
        requestSpecification = null;
        msgBody.clear();
        customHeaders.clear();
    }

    @And("I verify the response is in {string} format")
    public void iVerifyTheResponseIsInFormat(String responseType) {
        Assert.assertTrue("Response type is not " + responseType,
            response.getHeader("content-type").contains(responseType));
    }

    @And("I create a new user registration body using {string}")
    public void iCreateANewUserRegistrationBodyUsing(String fileName)
        throws IOException, ParseException {
        username = Random.getRandomString(10).toLowerCase();
        email = (Random.getRandomString(5) + "@gmail.com").toLowerCase();
        password = Random.getRandomString(10);

        msgBody.clear();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(TEST_DATA_DIR_PATH + fileName));
        msgBody = (JSONObject) obj;

        ((JSONObject) msgBody.get("user")).put("username", username);
        ((JSONObject) msgBody.get("user")).put("email", email);
        ((JSONObject) msgBody.get("user")).put("password", password);
    }

    @And("I verify successful user registration response")
    public void iVerifySuccessfulUserRegistrationResponse() throws ParseException {
        Assert.assertEquals("username is mismatched or not present in the response.", username,
            response.body().path("user.username"));
        Assert.assertEquals("email is mismatched or not present in the response.", email,
            response.body().path("user.email"));
        Assert.assertTrue("token is not present in the response.",
            ((JSONObject) ((JSONObject) new JSONParser().parse(response.getBody().asString()))
                .get("user")).containsKey("token"));
    }

    @And("I create a login body using {string}") public void iCreateALoginBodyUsing(String fileName)
        throws IOException, ParseException {
        msgBody.clear();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(TEST_DATA_DIR_PATH + fileName));
        msgBody = (JSONObject) obj;

        ((JSONObject) msgBody.get("user")).put("email", email);
        ((JSONObject) msgBody.get("user")).put("password", password);
    }

    @And("I verify successful login response") public void iVerifySuccessfulLoginResponse()
        throws ParseException {
        Assert.assertEquals("username is mismatched or not present in the response.", username,
            response.body().path("user.username"));
        Assert.assertEquals("email is mismatched or not present in the response.", email,
            response.body().path("user.email"));
        Assert.assertTrue("username is not present in the response.",
            ((JSONObject) ((JSONObject) new JSONParser().parse(response.getBody().asString()))
                .get("user")).containsKey("token"));
    }

    @And("I verify error message {string} for registration failure")
    public void iVerifyErrorMessageForRegistrationFailure(String errorMessage)
        throws ParseException {

        Assert.assertTrue("Error message is wrong for username.",
            response.body().path("errors.username").toString().contains(errorMessage));

        Assert.assertTrue("Error message is wrong for email.",
            response.body().path("errors.email").toString().contains(errorMessage));

    }

    @And("I verify error message {string} for login failure")
    public void iVerifyErrorMessageForLoginFailure(String errorMessage) throws ParseException {
        Assert.assertTrue("Error message is wrong for email or password.",
            (((JSONObject) ((JSONObject) new JSONParser().parse(response.getBody().asString()))
                .get("errors")).get("email or password")).toString().contains(errorMessage));
    }

    @And("I create a login body using {string} using email {string} and password {string}")
    public void iCreateALoginBodyUsingUsingEmailAndPassword(String fileName, String invalidEmail,
        String invalidPassword) throws IOException, ParseException {
        msgBody.clear();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(TEST_DATA_DIR_PATH + fileName));
        msgBody = (JSONObject) obj;

        ((JSONObject) msgBody.get("user")).put("email", invalidEmail);
        ((JSONObject) msgBody.get("user")).put("password", invalidPassword);
    }

    @And("I verify response has returned tags") public void iVerifyResponseHasReturnedTags()
        throws ParseException {
        JSONObject tagsResponse =
            (JSONObject) new JSONParser().parse(response.getBody().asString());
        JSONArray tags = (JSONArray) new JSONParser().parse(tagsResponse.get("tags").toString());
        Assert.assertTrue("Tags are not available.", tags.size() > 0);
    }
}
