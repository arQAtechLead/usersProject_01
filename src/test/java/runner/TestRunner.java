package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;

import java.util.ArrayList;
import java.util.List;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features", glue = { "stepdefinitions" }, plugin = { "pretty", "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json", "rerun:target/cucumber-reports/rerun.txt", "json:target/cucumber-reports/retry-cucumber.json" })
public class TestRunner {
    public static void main(String[] args) {
        int maxTestRuns = 10; // Maximum number of test runs
        int runCount;

        Result result;
        List<String> failedScenarios = new ArrayList<>();
        for (runCount = 0; runCount < maxTestRuns; runCount++) {
            result = JUnitCore.runClasses(TestRunner.class);

            for (Failure failure : result.getFailures()) {
                // Extract information about failed scenarios
                failedScenarios.add(failure.getTestHeader());
            }
        }

        if (failedScenarios.isEmpty()) {
            System.out.println("All scenarios passed.");
        } else {
            System.out.println("Failed scenarios:");
            for (String scenario : failedScenarios) {
                System.out.println(scenario);
            }
        }
    }
}
