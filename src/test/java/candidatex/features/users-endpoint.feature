@users @api @regression
Feature: Users endpoint

  Background:
    Given I use "https://qa-task.backbasecloud.com" as base URI

  @users1
  Scenario: Registration - Verify successful user registration
    Given I make REST service headers with the below fields
      | Content-Type                    | Authorization                      |
      | application/json; charset=utf-8 | Basic Y2FuZGlkYXRleDpxYS1pcy1jb29s |
    And I create a new user registration body using "createUser.json"
    When I make "POST" call to "/api/users"
    Then I get response code "200"
    And I verify successful user registration response

  @users2
  Scenario: Login - Verify successful Login
    Given I make REST service headers with the below fields
      | Content-Type                    | Authorization                      |
      | application/json; charset=utf-8 | Basic Y2FuZGlkYXRleDpxYS1pcy1jb29s |
    And I create a new user registration body using "createUser.json"
    When I make "POST" call to "/api/users"
    Then I get response code "200"
    And I create a login body using "login.json"
    When I make "POST" call to "/api/users/login"
    Then I get response code "200"
    And I verify successful login response

  @users3
  Scenario: Registration - Verify error message for new registration with existing username or email
    Given I make REST service headers with the below fields
      | Content-Type                    | Authorization                      |
      | application/json; charset=utf-8 | Basic Y2FuZGlkYXRleDpxYS1pcy1jb29s |
    And I create a new user registration body using "createUser.json"
    When I make "POST" call to "/api/users"
    Then I get response code "200"
    When I make "POST" call to "/api/users"
    Then I get response code "422"
    And I verify error message "is already taken" for registration failure

  @users4
  Scenario: Login - Verify error message for envalid username or password
    Given I make REST service headers with the below fields
      | Content-Type                    | Authorization                      |
      | application/json; charset=utf-8 | Basic Y2FuZGlkYXRleDpxYS1pcy1jb29s |
    And I create a login body using "login.json" using email "invalid543" and password "blahblah"
    When I make "POST" call to "/api/users/login"
    Then I get response code "422"
    And I verify error message "is invalid" for login failure

  @users5
  Scenario Outline: USERS Authorization - Verify user gets unauthorized error when authentication is not used
    Given I reset API configuration
    When I make "POST" call to "<END-POINT>"
    Then I get response code "401"
    Examples:
      | END-POINT        |
      | /api/users       |
      | /api/users/login |
