package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testbase.TestBaseClass;

public class AuthenticationTests extends TestBaseClass{
	@Test
	public void testRegisterUser() {
		
		
		app.logInfo("Registering User");
		
		app.click("signUp_login_linkText");
		app.validateTitle("signUp_login_page_title");
		app.type("sign_up_name_xpath", app.getProperty("name"));
		app.type("sign_up_email_xpath", app.getProperty("email"));
		app.click("signup_btn_xpath");
		app.waitForWebPageToLoad();
		
		// Validate user was redirected to proper page by Verifying current URL
		app.validateURL("https://www.automationexercise.com/signup");
		
		// Enter User Data
		String title = app.getProperty("title");
		String titleLocatorKey = "title_mr_xpath";
		if(title.equals("Mrs.")) {
			titleLocatorKey = "title_mrs_xpath";
		}
		
		app.click(titleLocatorKey);
		app.validateElementSelected(titleLocatorKey);
		
		
		
		app.clear("register_name_xpath");
		app.type("register_name_xpath", app.getProperty("name"));
		app.validateElementDisabled("register_email_xpath");  // Verify email cannot be modified
		app.type("register_password_xpath", app.getProperty("password"));
		
		app.selectValueFromDropdown("register_dob_days_dropdown_id", app.getProperty("day_of_birth"));
		app.validateValueSelectedInDropdown("register_dob_days_dropdown_id", app.getProperty("day_of_birth"));
		
		app.selectValueFromDropdown("register_dob_months_dropdown_id", app.getProperty("month_of_birth"));
		app.validateValueSelectedInDropdown("register_dob_months_dropdown_id", app.getProperty("month_of_birth"));
		
		app.selectValueFromDropdown("register_dob_years_dropdown_id", app.getProperty("year_of_birth"));
		app.validateValueSelectedInDropdown("register_dob_years_dropdown_id", app.getProperty("year_of_birth"));
		
		// Select Sign Up for newsletter and Receive special offers checkboxes
		String signUpNewsletter = app.getProperty("sign_up_newsletter");
		String receiveSpecialOffers = app.getProperty("receive_special_offers");
		if(signUpNewsletter.equals("true")) {
			app.click("sign_up_newsletter_id");
		}
		
		if(receiveSpecialOffers.equals("true")) {
			app.click("receive_special_offers_id");
		}
		
		app.type("user_firstname_xpath", app.getProperty("name"));
		app.type("user_lastname_xpath", app.getProperty("lastname"));
		app.type("user_company_id", app.getProperty("company"));
		app.type("user_address1_id", app.getProperty("address1"));
		app.type("user_address2_id", app.getProperty("address2"));
		
		app.selectValueFromDropdown("user_country_id", app.getProperty("country"));
		app.validateValueSelectedInDropdown("user_country_id", app.getProperty("country"));
		
		app.type("user_state_id", app.getProperty("state"));
		app.type("user_city_id", app.getProperty("city"));
		app.type("user_zipcode_id", app.getProperty("zipcode"));
		app.type("user_mobile_id", app.getProperty("mobile"));
		
		app.click("create_account_button_xpath");
		
		app.waitForWebPageToLoad();
		
		app.validateURL("https://www.automationexercise.com/account_created");
		app.validateText("account_created_text_xpath", "ACCOUNT CREATED!");
		app.click("continue_button_xpath");
		
		app.waitForWebPageToLoad();
		
		app.validateText("logged_in_as_xpath", "Logged in as " + app.getProperty("name"));
		
	}
	
	public void testRegisterExistingUser() {
		app.logInfo("Registering Existing User");
		
		app.click("signUp_login_linkText");
		app.validateTitle("signUp_login_page_title");
		app.type("sign_up_name_xpath", app.getProperty("name"));
		app.type("sign_up_email_xpath", app.getProperty("existing_email"));
		app.click("signup_btn_xpath");
		app.waitForWebPageToLoad();
		app.validateElementPresent("existing_email_label_xpath");
	}
	
	@Parameters ({"userType"})
	@Test
	public void testLogin(String userType, ITestContext context) {
		
		app.logInfo("Login Application with " + userType + " user");
		
		String email = app.getProperty("existing_email");
		String password = app.getProperty("password");
		
		if(userType.equals("invalid")) {
			email = app.getProperty("wrong_email");
			password = app.getProperty("wrong_password");
		}
		
		app.click("signUp_login_linkText");
		app.validateTitle("signUp_login_page_title");
		app.type("email_name", email);
		app.type("password_name", password);
		app.click("login_btn_xpath");
		app.waitForWebPageToLoad();
		
		if(userType.equals("valid")) {
			app.validateText("logged_in_as_xpath", "Logged in as " + app.getProperty("existing_user_name"));
		}else {
			app.validateElementPresent("email_or_password_incorrect_xpath");
		}
		
	}
	
	@Test
	public void testLogout() {
		app.logInfo("performing logout...");
		app.validateElementClickable("logout_account_xpath");
		app.click("logout_account_xpath");
		app.waitForWebPageToLoad();
		
		app.validateURL("https://www.automationexercise.com/login");
	}
	
	
	@Test
	public void testRemoveUser() {
		app.logInfo("deleting account...");
		app.click("delete_account_linkText");
		
		app.validateURL("https://www.automationexercise.com/delete_account");
		app.validateText("account_deleted_text_xpath", "ACCOUNT DELETED!");
		app.click("continue_button_xpath");
		
		app.waitForWebPageToLoad();
		
		app.validateTitle("main_page_title");
		app.validateElementNotDisplayed("logged_in_as_xpath");
	}
}
