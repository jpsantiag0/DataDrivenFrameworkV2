package keywords;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.ProfilesIni;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.asserts.SoftAssert;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import reports.ExtentReportManager;

public class GenericKeywords {
	
	public WebDriver driver = null;
	public Properties prop;
	public ExtentTest extentTest;
	public SoftAssert softAssert;
	

	
	public void openBrowser(String browserKey) {
		String browserName = prop.getProperty(browserKey);
		logInfo("Browser Name: "+browserName);
		if (browserName.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");
			
			ChromeOptions options = new ChromeOptions();

			String[] optionsList = { "--start-maximized", "--disable-infobars", "--disable-extensions", "--incognito",
					"--disable-notifications", "--ignore-certificate-errors" , "--remote-debugging-port=9222"};
			options.addArguments(optionsList);

			driver = new ChromeDriver(options);

		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/drivers/geckodriver");
			
			FirefoxOptions options = new FirefoxOptions();
			
			// Initialize profiles
			ProfilesIni profiles = new ProfilesIni();
			FirefoxProfile ffProfile = profiles.getProfile("TestUser");
			
			ffProfile.setPreference("dom.webnotifications.enabled", false);
			ffProfile.setAcceptUntrustedCertificates(true);
			ffProfile.setAssumeUntrustedCertificateIssuer(false);
			
			options.setProfile(ffProfile);
			

			driver = new FirefoxDriver(options);
		} else if (browserName.equalsIgnoreCase("safari")) {
			driver = new SafariDriver();
		} else {
			System.out.println("Unsupported browser: " + browserName);
			System.out.println("Using ChromeDriver by default");
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver");

			driver = new ChromeDriver();
		}

		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	}
	
	public void reportFailure(String msg, boolean isCriticalFailure) {
		logError(msg);
		takeScreenShot();
		softAssert.fail(msg);
		
		if (isCriticalFailure) {
			Reporter.getCurrentTestResult().getTestContext().setAttribute("isCriticalFailure", "true");
			reportAllFailures();
		}
	}
	
	public void reportFailure(String msg) {
		reportFailure(msg, false);
	}
	
	public void takeScreenShot() {
		// ScreenShot file name
		Date currentDate = new Date();
		
		// Format date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String formattedDate = dateFormat.format(currentDate);
		
		String screenshotFileName = formattedDate + ".png";
		String fullScreenshotPath = ExtentReportManager.screenShotPath + "/" + screenshotFileName;
		
		// take screenshot
		File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		
		try {
			FileUtils.copyFile(sourceFile, new File(fullScreenshotPath));
			
			// Put screenshot into extent report
			//logInfo("Screenshot -- " + extentTest.addScreenCaptureFromPath(fullScreenshotPath));
			extentTest.log(Status.FAIL, MarkupHelper.createLabel("Screenshot", ExtentColor.RED));
			extentTest.log(Status.FAIL, "<img src='" + fullScreenshotPath + "' style='width: 100%' />");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void reportAllFailures() {
		softAssert.assertAll();
	}
	
	public void setReport(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}
	
	public void logInfo(String msg) {
		extentTest.log(Status.INFO, msg);
	}
	
	public void logError(String msg) {
		extentTest.log(Status.FAIL, msg);
	}
	
	public void logWarning(String msg) {
		extentTest.log(Status.WARNING, msg);
	}
	
	public void logSkip(String msg) {
		extentTest.log(Status.SKIP, msg);
	}
	
	public void openURL(String URLKey) {
		logInfo("Opening URL: " + prop.getProperty(URLKey));
		driver.get(prop.getProperty(URLKey));
	}
	
	public void quitDriver() {
		driver.quit();
	}
	
	public void click(String locatorKey) {
		logInfo("Clicking by locator: "+prop.getProperty(locatorKey));
		getElement(locatorKey).click();
	}
	
	public void type(String locatorKey, String value) {
		logInfo("Typing "+value+" to "+prop.getProperty(locatorKey));
		getElement(locatorKey).sendKeys(value);

	}
	
	public void clear(String locatorKey) {
		logInfo("Clearing value from to "+prop.getProperty(locatorKey));
		getElement(locatorKey).clear();

	}
	
	public void selectValueFromDropdown(String locatorKey, String value) {
		Select dropdown = new Select(getElement(locatorKey));
		dropdown.selectByVisibleText(value);
	}
	
	public WebElement getElement(String locatorKey) {
		// Element is present
		if(!isElementPresent(locatorKey)) {
			// Report error
			
			String error_msg = "element is not present " + locatorKey;
			System.out.println(error_msg);
//			logError(error_msg);
//			reportFailure(error_msg);
		}
		
		// Element is visible
		if(!isElementVisible(locatorKey)) {
			
			String error_msg = "element is not visible " + locatorKey;
			System.out.println(error_msg);
//			logError(error_msg);
//			reportFailure(error_msg);
		}
		
		// Create WebElement and return WebElement
		WebElement element = driver.findElement(getLocator(locatorKey));
		return element;
	}
	
	public List<WebElement> getElements(String locatorKey){
		List<WebElement> elements = driver.findElements(getLocator(locatorKey));
		return elements;
	}
	
	public By getLocator(String locatorKey) {
		By by = null;
		
		if(locatorKey.endsWith("_id")) {
			by = By.id(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_xpath")) {
			by = By.xpath(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_linkText")) {
			by = By.linkText(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_partialLinkText")) {
			by = By.partialLinkText(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_name")) {
			by = By.name(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_className")) {
			by = By.className(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_tagName")) {
			by = By.tagName(prop.getProperty(locatorKey));
		}else if(locatorKey.endsWith("_css")) {
			by = By.cssSelector(prop.getProperty(locatorKey));
		}
		
		return by;
		
	}
	
	public boolean isElementPresent(String locatorKey) {
		// Explicit wait
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		
		try {
			wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
		}catch(Exception e) {
			reportFailure("Unable to locate element with Locator: "+getLocator(locatorKey));
			reportFailure(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean isElementVisible(String locatorKey) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(getLocator(locatorKey)));
		}catch(Exception e) {
			reportFailure("Unable to locate element with Locator: "+getLocator(locatorKey));
			reportFailure(e.getMessage());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void select() {
		
	}
	
	public String getText(String locatorKey) {
		return getElement(locatorKey).getText();
	}
	
	public void navigate() {
		
	}
	
	public void acceptAlert() {
		
	}
	
	public void dismissAlert(){
		
	}
	
	public void hoverOverElement(String locatorKey) {
		WebElement element = getElement(locatorKey);
		
		Actions action = new Actions(driver);
		action.moveToElement(element).build().perform();
		
	}
	
	public String getProperty(String dataKey) {
		return prop.getProperty(dataKey);
	}
	
	public void waitForWebPageToLoad() {
		logInfo("Waiting for page to load");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		int i=0;
		
		
		// Check JS status
		while (i!=10) {
			String state = (String) js.executeScript("return document.readyState;");
			// System.out.println("JS status"+state);
			
			if(state.equals("complete")) {
				break;
			}else {
				wait(2);
			}
			i++;
		}
		
		// Check for jQuery status
		i=0;
		while(i!=10) {
			Long d = (Long) js.executeScript("return jQuery.active;");
			//System.out.println("jQuery status" + d);
			if(d.longValue() == 0) {
				break;
			}else {
				wait(2);
			}
			i++;
		}
		
	}
	
	public void wait(int time) {
		try {
			Thread.sleep(time * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
