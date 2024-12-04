package reports;

import java.io.File;
import java.util.Date;

import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportManager {
	
	public static ExtentReports extentReports;
	public static String screenShotPath;
	
	// Initialize Report and Create Report
	public static ExtentReports getReports() {
		if(extentReports == null) {
			extentReports = new ExtentReports();
			
			Date date = new Date();
			String reportFolderName = date.toString().replaceAll(":", "_").replaceAll(" ", "-");
			String reportPath = System.getProperty("user.dir") + "/reports/" + reportFolderName;
			screenShotPath = reportPath + "/screenshots";
			
			File file = new File(screenShotPath);  // this contains both unique reports' name path and screenshots folder
			file.mkdirs();
			
			ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
			sparkReporter.config().setTheme(Theme.STANDARD);
			sparkReporter.config().setReportName("Production Test Report");
			sparkReporter.config().setDocumentTitle("Data Driven Test Report");
			sparkReporter.config().setEncoding("utf-8");
			
			extentReports.attachReporter(sparkReporter);
		}
		
		return extentReports;
	}
}
