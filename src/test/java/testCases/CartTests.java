package testCases;

import org.testng.ITestContext;
import org.testng.annotations.Test;

import testbase.TestBaseClass;

public class CartTests extends TestBaseClass{
	
	@Test
	public void testAddProductsToCart(ITestContext context) throws InterruptedException {
		
		app.logInfo("Adding product(s) to Cart");
		app.click("view_products_xpath");
		app.waitForWebPageToLoad();
		app.validateTitle("products_page_title");
		
		app.hoverOverProductAndAddToCart(app.getProperty("product1"));
		app.click("continue_shopping_button_xpath");
		
		app.hoverOverProductAndAddToCart(app.getProperty("product2"));
		
		app.click("view_cart_linkText");
		app.waitForWebPageToLoad();
		
	}
	
	@Test
	public void testViewCartContents(ITestContext context) {
		String product1 = app.getProperty("product1");
		String product2 = app.getProperty("product2");
		
		app.logInfo("Verifying Products were added to cart");
		app.validateTitle("cart_page_title");
		int rowNum = app.checkProductPresenceInCart("cart_table_id", product1);
		
		if(rowNum == -1) {
			app.reportFailure("Product [" + product1 + "] was not found in cart", true);
		}
		
		int rowNum2 = app.checkProductPresenceInCart("cart_table_id", product2);
		
		if(rowNum2 == -1) {
			app.reportFailure("Product [" + product2 + "] was not found in cart", true);
		}
		
		app.logInfo("Verifying Products' quantity in the cart");
		
		int quantity = app.findCurrentProductQuantity("cart_table_id", product1);
		app.logInfo("Product Name [" + product1 + "] - Product Quantity [" + quantity + "]");
		context.setAttribute("product1quantity", quantity);
		
		int quantity2 = app.findCurrentProductQuantity("cart_table_id", product2);
		app.logInfo("Product Name [" + product2 + "] - Product Quantity [" + quantity2 + "]");
		context.setAttribute("product2quantity", quantity2);
		
		app.logInfo("Verifying Products' Subtotal in Pre-Checkout cart");
		
		int product1Subtotal = app.getProductSubTotalPriceInCart("cart_table_id", product1);
		app.logInfo("Product Name [" + product1 + "] - Product Subtotal [" + product1Subtotal + "]");
		context.setAttribute("product1subtotal", product1Subtotal);
		
		int product2Subtotal = app.getProductSubTotalPriceInCart("cart_table_id", product2);
		app.logInfo("Product Name [" + product2 + "] - Product Quantity [" + product2Subtotal + "]");
		context.setAttribute("product2subtotal", product2Subtotal);
	}
}
