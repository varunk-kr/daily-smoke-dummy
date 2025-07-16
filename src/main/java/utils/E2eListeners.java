package utils;

import com.krogerqa.seleniumcentral.framework.listeners.TestNG_ConsoleRunner;
import com.krogerqa.seleniumcentral.framework.listeners.report.ImageCapture;
import com.krogerqa.seleniumcentral.framework.main.BaseTest;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class E2eListeners extends TestNG_ConsoleRunner implements ITestListener {

    public static final Map<String, String> testCaseIdOrderNumbers = new HashMap<>();    /*for storing the order numbers for each scenario respective to test case execution*/
    public static final Map<String, String> testCaseIdStoreNumbers = new HashMap<>();    /*for storing the store numbers for each scenario respective to test case execution*/
    protected static final Map<Boolean, String> totalCaseResults = new HashMap<>();        /*for getting all the total test cases results*/
    protected static final Map<Boolean, String> copyOfScenarioResults = new HashMap<>();  /*creating a copy so that scenario fails if any test case related to it is failed*/
    protected static Map<String, Boolean> totalScenarioResults = new HashMap<>();    /*for getting all the total scenarios results*/
    protected static Map<String, String> scenarioPackageMap = new HashMap<>();       /*for getting the package of all the scenarios*/
    protected static Map<String, String> scenarioDescriptionMap = new HashMap<>();   /*for getting the description of each scenario*/
    protected static Map<String, String> scenarioOrderNumbers = new HashMap<>();        /*for storing the order numbers for each scenario respective to email sender class*/
    protected static Map<String, String> scenarioStoreNumbers = new HashMap<>();        /*for storing the store numbers for each scenario respective to email sender class*/
    static Pattern pattern = Pattern.compile("Flow([A-Za-z0-9]+)Test");        /*for matching the classNames in this format*/
    protected int testCases = 0;                                              /*for getting the test cases count*/

    @Override
    public void onTestSuccess(ITestResult result) {
        /*Implemented by Omar*/
        BaseTest.reporter(result);
        this.log("    ***Result = PASSED");
        this.log(result.getEndMillis(), "END  -> " + result.getName());
        this.log("\n---\n");
        super.onTestSuccess(result);

        /* Implemented by E2E*/
        testCases++;
        String fullPackageName = result.getInstance().getClass().getPackage().getName();
        String packageName = fullPackageName.substring(fullPackageName.lastIndexOf('.') + 1);
        String description = result.getMethod().getDescription();
        String scenarioName = result.getInstance().getClass().getName();
        Matcher matcher = pattern.matcher(scenarioName);
        if (matcher.find()) {
            scenarioName = matcher.group(1);
        }
        scenarioPackageMap.put(scenarioName, packageName);
        scenarioDescriptionMap.put(scenarioName, description);
        totalCaseResults.put(true, scenarioName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        /*Implemented by Omar*/
        BaseTest.reporter(result);
        if (!this.getTestMessage(result).equals("")) {
            String var10001 = this.getTestMessage(result);
            this.log(var10001 + "\n");
        }
        this.log("    ***Result = FAILED\n");
        long var4 = result.getEndMillis();
        String var10002 = result.getInstanceName();
        this.log(var4, "END  -> " + var10002 + "." + result.getName());
        this.log("\n---\n");
        try {
            result.setAttribute("testBitmap", ImageCapture.screenShot(result));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        super.onTestFailure(result);


        /*Implemented by E2E*/
        testCases++;
        String fullPackageName = result.getInstance().getClass().getPackage().getName();
        String packageName = fullPackageName.substring(fullPackageName.lastIndexOf('.') + 1);
        String description = result.getMethod().getDescription();
        String scenarioName = result.getInstance().getClass().getName();
        Matcher matcher = pattern.matcher(scenarioName);
        if (matcher.find()) {
            scenarioName = matcher.group(1);
        }
        scenarioPackageMap.put(scenarioName, packageName);
        scenarioDescriptionMap.put(scenarioName, description);
        totalCaseResults.put(false, scenarioName);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        /*Implemented by Omar*/
        BaseTest.reporter(result);
        if (!this.getTestMessage(result).equals("")) {
            String var10001 = this.getTestMessage(result);
            this.log(var10001 + "\n");
        }
        this.log("    ***Result = SKIPPED\n");
        long var4 = result.getEndMillis();
        String var10002 = result.getInstanceName();
        this.log(var4, "END  -> " + var10002 + "." + result.getName());
        this.log("\n---\n");
        try {
            result.setAttribute("testBitmap", ImageCapture.screenShot(result));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        super.onTestSkipped(result);


        /*Implemented by E2E*/
        testCases++;
        String fullPackageName = result.getInstance().getClass().getPackage().getName();
        String packageName = fullPackageName.substring(fullPackageName.lastIndexOf('.') + 1);
        String description = result.getMethod().getDescription();
        String scenarioName = result.getInstance().getClass().getName();
        Matcher matcher = pattern.matcher(scenarioName);
        if (matcher.find()) {
            scenarioName = matcher.group(1);
        }
        scenarioPackageMap.put(scenarioName, packageName);
        scenarioDescriptionMap.put(scenarioName, description);
        totalCaseResults.put(false, scenarioName);
    }

    @Override
    public void onFinish(ITestContext context) {
        /*Implemented by Omar*/
        int var10001 = this.getPassedTests().size();
        this.log("\nTotal Passed = " + var10001 + ", Total Failed = " + this.getFailedTests().size() + ", Total Skipped = " + this.getSkippedTests().size() + "\n");
        super.onFinish(context);


        /*Implemented by E2E*/
        copyOfScenarioResults.putAll(totalCaseResults);
        for (boolean scenarioResult : totalCaseResults.keySet()) {
            String scenarioName = totalCaseResults.get(scenarioResult);
            totalScenarioResults.put(scenarioName, scenarioResult);
        }
        for (boolean scenarioResult : copyOfScenarioResults.keySet()) {
            String scenarioName = copyOfScenarioResults.get(scenarioResult);
            if (!scenarioResult) {
                totalScenarioResults.put(scenarioName, false);
            }
        }
        totalCaseResults.clear();

        /* For storing the results in a json file*/
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("scenarioPackageMap", scenarioPackageMap);
        jsonObject.put("scenarioDescriptionMap", scenarioDescriptionMap);
        jsonObject.put("finalScenarioResults", totalScenarioResults);
        jsonObject.put("testCaseIdOrderNumbers", testCaseIdOrderNumbers);
        jsonObject.put("testCaseIdStoreNumbers", testCaseIdStoreNumbers);
        try (FileWriter file = new FileWriter("src/test/resources/test-results/results.json")) {
            file.write(jsonObject.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
