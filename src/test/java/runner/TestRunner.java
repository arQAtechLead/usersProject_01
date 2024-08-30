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
        int maxRerunAttempts = 3; // Maximum number of rerun attempts
        int rerunCount = 0;

        while (rerunCount < maxRerunAttempts) {
            Result result = JUnitCore.runClasses(TestRunner.class);
            List<String> failedScenarios = new ArrayList<>();

            for (Failure failure : result.getFailures()) {
                // Extract information about failed scenarios
                failedScenarios.add(failure.getTestHeader());
            }

            if (failedScenarios.isEmpty()) {
                System.out.println("All scenarios passed.");
                break; // No need to rerun
            } else {
                System.out.println("Failed scenarios:");
                for (String scenario : failedScenarios) {
                    System.out.println(scenario);
                }
                System.out.println("Rerunning failed scenarios...");
                rerunCount++;
            }
        }

        if (rerunCount == maxRerunAttempts) {
            System.out.println("Maximum number of rerun attempts reached. Exiting...");
        }
    }
}
