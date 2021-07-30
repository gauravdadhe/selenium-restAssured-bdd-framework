# QaTask
QA-Task Backbase Automation project

## Technical stack used.
  1. Required scripting language: Java 
  2. JDK: JDK 11
  3. Maven: 3.6.1
  4. Required testing tool: Rest-assured, Selenium, Cucumber-BDD framework with Junit
  5. CI platform: git, jenkins

## How to run the tests locally
The tests can be run locally using below command in terminal
```java
mvn clean install
mvn verify -Pregression
```

The reports of the execution are available at,
* html --> Available at *target/aggregatedResults/index.html*

![image](https://user-images.githubusercontent.com/23377173/127638269-89ca257f-0a4b-4eae-9b09-6290c69216f2.png)

## How to run the test in a CI/CD pipeline
_jenkins.pipeline_ file is available in the project. This file can be used for executing the tests using Jenkins.
There are 3 stages available in this file.
* Git pull --> *To pull the project from github*
* ExecuteTests --> *To execute the tests*

Note: This is a draft feature.
