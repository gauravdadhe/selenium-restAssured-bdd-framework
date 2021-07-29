@conduit @regression
Feature: Conduit WebSpecs

  Background:
    Given I login to Conduit

  @conduit1
  Scenario: HOME PAGE - Verify popular tags
    Given I capture the first popular tag
    When I click on first popular tag
    Then I verify navigation for the selected tag is opened
    And I verify the navigation has feeds

  @conduit2
  Scenario: HOME PAGE - Verify global feeds
    When I click on 'Global Feed' navigation
    Then I verify 'Global Feed' navigation is opened
    And I verify the navigation has feeds

  @conduit3
  Scenario: HOME PAGE - Verify pagination for more than 10 feeds on Global feeds navigation
    When I click on 'Global Feed' navigation
    Then I verify page link "1" is selected
    When I click on page "2"
    Then I verify page link "2" is selected

  @conduit4
  Scenario: SIGN IN - Verify error on invalid user or password
    When I click on "Sign in" from top navigation bar
    And I enter credentials as user "invalid@lmn.com" and password "invalid"
    When I click on 'Sign in' button
    Then I verify error message 'email or password is invalid' is displayed
