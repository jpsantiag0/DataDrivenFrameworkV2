package listener;

import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;

public class TestNGListener implements ITestListener{
	
	public void onTestFailure(ITestResult result) {
		ExtentTest extentTest = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		extentTest.fail(result.getThrowable().getMessage());
	}
	
	public void onTestSuccess(ITestResult result) {
		ExtentTest extentTest = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		extentTest.pass("Test Success: " + result.getName());
	}
	
	public void onTestSkipped(ITestResult result) {
		ExtentTest extentTest = (ExtentTest) result.getTestContext().getAttribute("extentTest");
		extentTest.skip(result.getName() + " :: Test Skipped Due to Critical Error in Previous Test");
	}
	
}
