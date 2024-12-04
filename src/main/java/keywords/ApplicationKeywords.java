package keywords;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

public class ApplicationKeywords extends ValidationKeywords {

	public ApplicationKeywords() {
		prop = new Properties();

		try {
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir") + "/src/test/resources/Project.properties");
			prop.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		softAssert = new SoftAssert();
	}

	public void login() {

	}

	public void addProductToCart() {

	}

	public void hoverOverProductAndAddToCart(String productName) {
		String productXpath = "//div[@class='productinfo text-center']/p[contains(text(), '" + productName
				+ "')]/preceding-sibling::img";
		String hoveredAddToCartButtonXpath = "//div[@class='overlay-content']/p[contains(text(), '" + productName
				+ "')]/following-sibling::a";
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		
		logInfo("Scrolling down...");
		WebElement element = driver.findElement(By.xpath(productXpath));
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		
		logInfo("Hovering over - " + productXpath);
		
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(productXpath)));
			Actions action = new Actions(driver);
			action.moveToElement(element).build().perform();
			logInfo("Hovered over! - " + productXpath);
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Couldn't find element locator to hover: " + productXpath, true);
		}
		
		
		
		logInfo("Trying to Click - " + hoveredAddToCartButtonXpath);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(By.xpath(hoveredAddToCartButtonXpath)));
			WebElement hoveredAddToCartButton = driver.findElement(By.xpath(hoveredAddToCartButtonXpath));
			hoveredAddToCartButton.click();
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Couldn't click on overlay element: " + hoveredAddToCartButtonXpath, true);
		}
		logInfo("Clicked - " + hoveredAddToCartButtonXpath);

	}

	public int checkProductPresenceInCart(String locatorKey, String productName) {
		
		WebElement table = getElement(locatorKey);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		
		for(int rNum=0; rNum<rows.size(); rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(int cNum=0; cNum<cells.size(); cNum++) {
				WebElement cell = cells.get(cNum);
				//System.out.println("Text " + cell.getText());
				if(!cell.getText().trim().equals("")) {
					if(cell.getText().startsWith(productName)) {
						return rNum;
					}
				}
			}
		}
		return -1;
		
	}
	
	public int findCurrentProductQuantity(String locatorKey, String productName) {
		int rowNum = checkProductPresenceInCart(locatorKey, productName);
		
		String locatorValue = getProperty(locatorKey);
		
		if(rowNum == -1) {
			logInfo("Product Quantity is 0 as Product Name [" + productName + "] is not present in cart");
			return 0;
		}
		
		String selectorPrefix = "table#" + locatorValue;
		if(locatorKey.equals("checkout_cart_table_id")) {
			selectorPrefix = "div#" + locatorValue +" > table";
		}
		
		String quantity = driver.findElement(By.cssSelector(selectorPrefix + " > tbody > tr:nth-child("+ rowNum +") > td:nth-child(4)")).getText();
		String msg = "Product Name [" + productName + "] - Product Quantity [" + quantity + "]";
		System.out.println(msg);
		logInfo(msg);
		
		return Integer.parseInt(quantity);
		
		
	}
	
	public int getTotalPriceInCart(String locatorKey) {
		String totalAmount = "Total Amount";
		int rowNum = checkProductPresenceInCart(locatorKey, totalAmount);
		
		String locatorValue = getProperty(locatorKey);
		
		if(rowNum == -1) {
			logInfo("Product Quantity is 0 as Product Name [" + totalAmount + "] is not present in cart");
			return 0;
		}
		
		String selectorPrefix = "table#" + locatorValue;
		if(locatorKey.equals("checkout_cart_table_id")) {
			selectorPrefix = "div#" + locatorValue +" > table";
		}
		
		String totalText = driver.findElement(By.cssSelector(selectorPrefix + " > tbody > tr:nth-child("+ rowNum +") > td:nth-child(4)")).getText();
		System.out.println("Total: " + totalText);
		String total = totalText.replaceAll("[^0-9]", "");
		System.out.println("Total Numbers only: " + total);
		String msg = "Field [" + totalAmount + "] - Total Price [" + total + "]";
		System.out.println(msg);
		logInfo(msg);
		
		return Integer.parseInt(total);
	}
	
	public int getProductSubTotalPriceInCart(String locatorKey, String productName) {
		int rowNum = checkProductPresenceInCart(locatorKey, productName);
		
		String locatorValue = getProperty(locatorKey);
		
		if(rowNum == -1) {
			logInfo("Product Quantity is 0 as Product Name [" + productName + "] is not present in cart");
			return 0;
		}
		
		String selectorPrefix = "table#" + locatorValue;
		if(locatorKey.equals("checkout_cart_table_id")) {
			selectorPrefix = "div#" + locatorValue +" > table";
		}
		
		String subtotalText = driver.findElement(By.cssSelector(selectorPrefix + " > tbody > tr:nth-child("+ rowNum +") > td:nth-child(5)")).getText();
		System.out.println("Subtotal: " + subtotalText);
		String subtotal = subtotalText.replaceAll("[^0-9]", "");
		System.out.println("Subtotal Numbers only: " + subtotal);
		String msg = "Product Name [" + productName + "] - Product Subtotal Price [" + subtotal + "]";
		System.out.println(msg);
		logInfo(msg);
		
		return Integer.parseInt(subtotal);
	}
	
	public int checkProductQuantityInCart(String locatorKey, String productName) {
		WebElement table = getElement(locatorKey);
		List<WebElement> rows = table.findElements(By.tagName("tr"));
		
		for(int rNum=0; rNum<rows.size(); rNum++) {
			WebElement row = rows.get(rNum);
			List<WebElement> cells = row.findElements(By.tagName("td"));
			for(int cNum=0; cNum<cells.size(); cNum++) {
				WebElement cell = cells.get(cNum);
				if(!cell.getText().trim().equals("")) {
					if(cell.getText().startsWith(productName)) {
						return(rNum + 1);
					}
				}
			}
		}
		return -1;
		
	}

}
