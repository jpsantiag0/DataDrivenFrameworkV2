package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import testbase.TestBaseClass;

public class CheckoutTests extends TestBaseClass{
	
	@Test
	public void testProceedToCheckout() {
		app.logInfo("Proceeding to checkout cart");
		app.click("proceed_to_checkout_button_xpath");
		app.waitForWebPageToLoad();
		
	}
	
	@Parameters ({"userType"})
	@Test
	public void testVerifyOrderDetails(String userType, ITestContext context) {
		
		
		String fullName = app.getProperty("title") + " " + app.getProperty("name") + " " + app.getProperty("lastname");
		String cityStateZip = app.getProperty("city") + " " + app.getProperty("state") + " " + app.getProperty("zipcode");
		String company = app.getProperty("company");
		String address1 = app.getProperty("address1");
		String address2 = app.getProperty("address2");
		String country = app.getProperty("country");
		String mobile = app.getProperty("mobile");
		
		String product1 = app.getProperty("product1");
		String product2 = app.getProperty("product2");
		
		if(userType.equals("valid")) {
			app.logInfo("Validating info from Existing User");
			fullName = app.getProperty("existing_user_title") + " " + app.getProperty("existing_user_name") + " " + app.getProperty("existing_user_lastname");
			cityStateZip = app.getProperty("existing_user_city") + " " + app.getProperty("existing_user_state") + " " + app.getProperty("existing_user_zipcode");
			company = app.getProperty("existing_user_company");
			address1 = app.getProperty("existing_user_address1");
			address2 = app.getProperty("existing_user_address2");
			country = app.getProperty("existing_user_country");
			mobile = app.getProperty("existing_user_mobile");
		}
		
		app.validateURL("https://www.automationexercise.com/checkout");
		
		app.logInfo("Checking Delivery Address");
		
		app.validateTextContains("delivery_address_name_xpath", fullName);
		app.validateTextContains("delivery_address_company_xpath", company);
		app.validateTextContains("delivery_address_address1_xpath", address1);
		app.validateTextContains("delivery_address_address2_xpath", address2);
		app.validateTextContains("delivery_address_city_state_xpath", cityStateZip);
		app.validateTextContains("delivery_address_country_xpath", country);
		app.validateTextContains("delivery_address_phone_xpath", mobile);
		
		app.logInfo("Checking Billing Address");
		
		app.validateTextContains("billing_address_name_xpath", fullName);
		app.validateTextContains("billing_address_company_xpath", company);
		app.validateTextContains("billing_address_address1_xpath", address1);
		app.validateTextContains("billing_address_address2_xpath", address2);
		app.validateTextContains("billing_address_city_state_xpath", cityStateZip);
		app.validateTextContains("billing_address_country_xpath", country);
		app.validateTextContains("billing_address_phone_xpath", mobile);
		
		
		app.logInfo("Verifying Products presence in Checkout cart");
		app.validateTitle("cart_page_title");
		int rowNum = app.checkProductPresenceInCart("checkout_cart_table_id", product1);
		
		if(rowNum == -1) {
			app.reportFailure("Product [" + product1 + "] was not found in cart", true);
		}
		
		int rowNum2 = app.checkProductPresenceInCart("checkout_cart_table_id", product2);
		
		if(rowNum2 == -1) {
			app.reportFailure("Product [" + product2 + "] was not found in cart", true);
		}
		
		app.logInfo("Verifying Products' quantity in Checkout cart");
		
		int quantity = app.findCurrentProductQuantity("checkout_cart_table_id", product1);
		app.logInfo("Product Name [" + product1 + "] - Product Quantity [" + quantity + "]");
		int expectedQuantity = (Integer) context.getAttribute("product1quantity");
		
		if(quantity != expectedQuantity) {
			app.reportFailure("Product [" + product1 + "] did not show the expected quantity", true);
		}
		
		int quantity2 = app.findCurrentProductQuantity("checkout_cart_table_id", product2);
		app.logInfo("Product Name [" + product2 + "] - Product Quantity [" + quantity2 + "]");
		int expectedQuantity2 = (Integer) context.getAttribute("product2quantity");
		
		if(quantity2 != expectedQuantity2) {
			app.reportFailure("Product [" + product2 + "] did not show the expected quantity", true);
		}
		
		
		app.logInfo("Verifying Products' Subtotal in Checkout cart");
		
		int product1Subtotal = app.getProductSubTotalPriceInCart("checkout_cart_table_id", product1);
		app.logInfo("Product Name [" + product1 + "] - Product Subtotal [" + product1Subtotal + "]");
		int expectedProduct1Subtotal = (Integer) context.getAttribute("product1subtotal");
		
		if(product1Subtotal != expectedProduct1Subtotal) {
			app.reportFailure("Product [" + product1 + "] Subtotal didn't match at Pre-checkout and Checkout", true);
		}
		
		int product2Subtotal = app.getProductSubTotalPriceInCart("checkout_cart_table_id", product2);
		app.logInfo("Product Name [" + product2 + "] - Product Quantity [" + product2Subtotal + "]");
		int expectedProduct2Subtotal = (Integer) context.getAttribute("product2subtotal");
		
		if(product2Subtotal != expectedProduct2Subtotal) {
			app.reportFailure("Product [" + product2 + "] Subtotal didn't match at Pre-checkout and Checkout", true);
		}
		
		int totalAmount = app.getTotalPriceInCart("checkout_cart_table_id");
		int expectedTotalAmount = product1Subtotal + product2Subtotal;
		
		if(totalAmount != expectedTotalAmount) {
			app.reportFailure("Total Amount at Checkout is not correct", true);
		}
		
	}
	
	
	@Test
	public void testEnterCommentsAndPlaceOrder(ITestContext context) {

		app.logInfo("Adding text to comment section");
		app.type("checkout_comment_box_name", "test comment");
		
		app.logInfo("Proceeding to Place Order");
		app.click("place_order_button_xpath");
		app.waitForWebPageToLoad();
		
	}
	
	@Test
	public void testEnterPaymentDetails() {
		app.logInfo("Proceeding to do the Payment");
		
		app.validateTitle("payment_page_title");
		
		app.logInfo("Filling in Payment data");
		app.type("name_on_card_box_name", app.getProperty("name") + " " + app.getProperty("lastname"));
		app.type("card_number_box_name", app.getProperty("card_number"));
		app.type("cvc_box_name", app.getProperty("card_cvc"));
		app.type("expiration_month_box_name", app.getProperty("card_expiration_month"));
		app.type("expiration_year_box_name", app.getProperty("card_expiration_year"));
		
		app.click("pay_and_confirm_button_id");
		
		app.waitForWebPageToLoad();
		
	}
	
	@Test
	public void testOrderConfirmation() {
		app.logInfo("Confirming Order has been placed");
		
		app.validateTitle("order_placed_page_title");
		
		app.validateText("order_placed_text_xpath", "ORDER PLACED!");
		app.validateElementDisplayed("download_invoice_button_xpath");
		
		app.click("continue_button_xpath");
		app.waitForWebPageToLoad();
		
	}

}
