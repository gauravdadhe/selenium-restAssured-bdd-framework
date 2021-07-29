@tags @api @regression
Feature: Tags endpoint

  Background:
    Given I use "https://qa-task.backbasecloud.com" as base URI

  @tags1
  Scenario: Tags - Verify if user gets list of tags
    Given I make REST service headers with the below fields
      | Authorization                      |
      | Basic Y2FuZGlkYXRleDpxYS1pcy1jb29s |
    When I make "GET" call to "/api/tags"
    Then I get response code "200"
    And I verify response has returned tags

  @tags2
  Scenario: TAGS Authorization - Verify user gets unauthorized error when authentication is not used
    Given I reset API configuration
    When I make "GET" call to "/api/tags"
    Then I get response code "401"
