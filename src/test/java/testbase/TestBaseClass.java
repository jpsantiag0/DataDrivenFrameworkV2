package testbase;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestContext;
import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import reports.ExtentReportManager;

import keywords.ApplicationKeywords;

public class TestBaseClass {

	public ApplicationKeywords app;
	public String number;
	public ExtentReports extentReport;
	public ExtentTest extentTest;

	@BeforeTest
	public void beforeTest(ITestContext context) {
		// System.out.println("=====Before Test");
		// Single App Object for Single test
		// Initialize and Share for all test cases
		app = new ApplicationKeywords();
		context.setAttribute("app", app);

		extentReport = ExtentReportManager.getReports();
		String testName = context.getCurrentXmlTest().getName();
		// System.out.println("Test Name: "+testName);
		extentTest = extentReport.createTest(testName);
		extentTest.log(Status.INFO, "Starting Test: " + testName);

		app.setReport(extentTest);

		context.setAttribute("extentReport", extentReport);
		context.setAttribute("extentTest", extentTest);
		
	}

	@AfterTest
	public void afterTest(ITestContext context) {
 		app = (ApplicationKeywords) context.getAttribute("app");

		if (app != null) {
			app.quitDriver();
		}

		extentReport = (ExtentReports) context.getAttribute("extentReport");

		if (extentReport != null) {
			extentReport.flush();
		}

	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(ITestContext context) {
		app = (ApplicationKeywords) context.getAttribute("app");
		extentReport = (ExtentReports) context.getAttribute("extentReport");
		extentTest = (ExtentTest) context.getAttribute("extentTest");
		
		String criticalFailure = (String) context.getAttribute("isCriticalFailure");
		if(criticalFailure != null && criticalFailure.equals("true")) {
			String error_msg = "Critical Failure in previous test method";
			app.logSkip(error_msg);
			throw new SkipException(error_msg);
		}
		
	}

	@AfterMethod(alwaysRun = true)
	public void afterMethod(ITestContext context) {
		
		app = (ApplicationKeywords) context.getAttribute("app");

		app.reportAllFailures();

	}
}
