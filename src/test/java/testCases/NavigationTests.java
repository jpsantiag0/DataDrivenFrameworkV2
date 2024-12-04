package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import testbase.TestBaseClass;

public class NavigationTests extends TestBaseClass{
	@Test
	public void testLaunchBrowserAndNavigateToURL(ITestContext context) {
		
		app.openBrowser("browser_name");
		app.openURL("url");
		app.waitForWebPageToLoad();
		
	}
	
	@Test
	public void testHomePageVisibility(ITestContext context) {
		
		app.validateTitle("main_page_title");
		
	}
}
