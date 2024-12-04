package keywords;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class ValidationKeywords extends GenericKeywords {
	public void validateTitle(String expectedTitleKey) {
		logInfo("Expected Title: " + prop.getProperty(expectedTitleKey));
		Assert.assertEquals(driver.getTitle(), prop.getProperty(expectedTitleKey));
	}

	public void validateText(String locatorKey, String expectedText) {

		String actualText = getText(locatorKey);
		logInfo("Validating Actual Text[" + actualText + "] equals to Expected Text[" + expectedText + "]");
		if (!actualText.equals(expectedText)) {
			reportFailure("Text from Locator: " + locatorKey + " was not expected. Actual Text[" + actualText
					+ "], Expected Text[" + expectedText + "]", true);
		}
	}

	public void validateTextContains(String locatorKey, String expectedText) {

		String actualText = getText(locatorKey);
		logInfo("Validating Actual Text[" + actualText + "] equals to Expected Text[" + expectedText + "]");
		if (!actualText.contains(expectedText)) {
			reportFailure("Text from Locator: " + locatorKey + " does not contain expected Text. Actual Text["
					+ actualText + "], Expected Text[" + expectedText + "]", true);
		}
	}

	public void validateURL(String expectedURL) {
		logInfo("Validating User was redirected to URL " + expectedURL);
		String actualURL = driver.getCurrentUrl();
		if (!actualURL.equals(expectedURL)) {
			reportFailure("Failed to validate URL. Expected URL [" + expectedURL + "] - Actual URL [" + actualURL + "]",
					true);
		}
	}

	public void validateElementPresent(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is present");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		wait.until(ExpectedConditions.presenceOfElementLocated(getLocator(locatorKey)));
	}

	public void validateElementClickable(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is clickable");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.elementToBeClickable(getLocator(locatorKey)));
	}

	public void validateElementDisplayed(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is displayed");
		if (!getElement(locatorKey).isDisplayed()) {
			reportFailure("Element Locator " + locatorKey + " is not displayed as expected");
		}
	}

	public void validateElementNotDisplayed(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is NOT displayed");

		if (driver.findElements(getLocator(locatorKey)).size() > 0) {
			reportFailure("Element Locator " + locatorKey + " shouldn't be displayed");
		} else {
			logInfo("Element Locator " + locatorKey + " is NOT displayed as expected");
		}

	}

	public void validateElementSelected(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is Selected");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		try {
			wait.until(ExpectedConditions.elementToBeSelected(getLocator(locatorKey)));
		} catch (Exception e) {
			e.printStackTrace();
			reportFailure("Element Locator " + locatorKey + " is not selected as expected");
		}

	}

	public void validateValueSelectedInDropdown(String locatorKey, String expectedValue) {
		Select dropdown = new Select(getElement(locatorKey));
		String actualValue = dropdown.getFirstSelectedOption().getText();

		if (!actualValue.equals(expectedValue)) {
			reportFailure("Element Locator " + locatorKey + " did not show expected value selected. Actual Value["
					+ actualValue + "], Expected Value[" + expectedValue + "]");
		}

	}

	public void validateElementNotClickable(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is Unclickable ");
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(getLocator(locatorKey))));
	}

	public void validateElementDisabled(String locatorKey) {
		logInfo("Validating Element Locator " + locatorKey + " is Disabled ");
		if (getElement(locatorKey).isEnabled()) {
			reportFailure("Element Locator " + locatorKey + " is not Disabled as expected");
		}

	}
}
