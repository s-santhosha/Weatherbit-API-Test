package test.java.Base;

import test.java.utils.extentReportFactory.ExtentReportFactory;
import test.java.utils.excelReader.*;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

public class BaseTest implements IHookable {

    public static Properties prop;

    protected ExtentTest testReporter;

    protected ResponseSpecification checkStatusCodeAndContentType =
            new ResponseSpecBuilder().
                    expectStatusCode(200).
                    expectContentType(ContentType.JSON).
                    build();
    ExcelReader excel;

    public BaseTest() {
        try {
            prop = new Properties();
            FileInputStream ip = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/config/config.properties");
            prop.load(ip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterMethod
    public void cleanUp(ITestResult result, Method method) {
        String testname = getTestName(result);
        testReporter.log(LogStatus.INFO, "After test:" + testname);
        if (result.getStatus() == ITestResult.FAILURE) {
            testReporter.log(LogStatus.FAIL, "Test Failed: " + result.getThrowable().getMessage());
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            testReporter.log(LogStatus.PASS, "Test Passed");
        } else if (result.getStatus() == ITestResult.SKIP) {
            testReporter.log(LogStatus.SKIP, "Test Skipped");
        } else {
            testReporter.log(LogStatus.ERROR, "Error while executing test");
        }
        testReporter = null;
        ExtentReportFactory.closeTest(testname);
//        driver.close();

    }


    private String getTestName(ITestResult testResult) {
        String name = testResult.getName();
        Object[] parameters = testResult.getParameters();
        StringBuilder testName = new StringBuilder();

        testName.append(name);
        testName.append("(");
        for (int i = 0; i < parameters.length; i++) {
            testName.append("[" + parameters[i].toString() + "]");
            if (i != parameters.length - 1) {
                testName.append(",");
            }
        }
        testName.append(")");

        return testName.toString();
    }

    @AfterSuite
    public void afterSuite() {
        ExtentReportFactory.closeReport();
    }

    public void run(IHookCallBack callBack, ITestResult testResult) {
        String description = testResult.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class).description();
        String testName = getTestName(testResult);
        ExtentReportFactory.getTest(testName, description);
        testReporter = ExtentReportFactory.getTest();
        testReporter.log(LogStatus.INFO, "Started test :" + testName);
        System.out.println("Method: " + testName);
        callBack.runTestMethod(testResult);

    }

    //Load all the data in the String[][] using the getDataFromSheet method of ExcelReader class
    public String[][] getData(String sheetname, String excelname) {
        String path = System.getProperty("user.dir") + "/src/test/resources/testdata/" + excelname;
        excel = new ExcelReader(path);
        String[][] data = excel.getDataFromSheet(sheetname, excelname);
        return data;
    }

}
