package testCases;

import org.testng.annotations.Test;

import keywords.*;

public class LoginTestDeleteMe extends ApplicationKeywords {

	@Test
	public void loginTest() {

		ApplicationKeywords app = new ApplicationKeywords();

		app.openBrowser("browser_name");
		app.openURL("url");
		app.validateTitle("main_page_title");
		app.click("signUp_login_linkText");
		app.validateTitle("signUp_login_page_title");
		app.type("email_name", "juanpsantiagolopez@gmail.com");
		app.type("password_name", "h3ll0W0rld!");
		app.click("login_btn_xpath");
			
		app.quitDriver();

	}

}
