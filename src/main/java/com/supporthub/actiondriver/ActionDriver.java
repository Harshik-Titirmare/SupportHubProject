package com.supporthub.actiondriver;

import java.io.File;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.supporthub.base.BaseClass;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;

	// Constructor to initialize WebDriver and explicit wait based on config
	// properties
	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		System.out.println("Webdriver instance is created.");
	}

	// Method to click an element after waiting for it to be clickable
	public void click(By by) {
		try {
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
		} catch (Exception e) {
			System.out.println("Unable to Click the Element: " + e.getMessage());
			throw e;
		}
	}

	// Method to enter text into an input field after ensuring visibility
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			System.out.println("Unable to enter the Value: " + e.getMessage());
			throw e;
		}
	}

	// Method to get text from a web element
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println("Unable to get The Text: " + e.getMessage());
			return "";
		}
	}

	// Method to compare actual text with expected text and return boolean result
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				System.out.println("The text are Matching: " + actualText + " Equals " + expectedText);
				return true;
			} else {
				System.out.println("The text are Not Matching: " + actualText + " Not Equals " + expectedText);
			}
		} catch (Exception e) {
			System.out.println("Unable to Compare the text: " + e.getMessage());
			return false;
		}
		return false;
	}

	// Method to check if an element is displayed on the page
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			System.out.println("Element is not Displayed: " + e.getMessage());
			return false;
		}
	}

	// Method to wait for the complete page load using JavaScript readyState
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page is Loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page did not load within " + timeOutInSec + " Seconds. Exception: " + e.getMessage());
		}
	}

	// Method to scroll the page until the target element comes into view
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			System.out.println("Unable to locate Element for scrolling: " + e.getMessage());
		}
	}

	// Method to handle unexpected browser alert popups if present
	public void handleAlertIfPresent() {
		try {
			WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
			alertWait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
			System.out.println("Alert popup accepted successfully.");
		} catch (Exception e) {
			System.out.println("No alert popup appeared, proceeding normally.");
		}
	}

	// Private helper method to wait for an element to become clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("Element is not clickable: " + e.getMessage());
		}
	}

	// Private helper method to wait for an element to become visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Element is not Visible: " + e.getMessage());
		}
	}

	// Generic method to verify if a file has successfully downloaded in the local
	// Downloads folder
	public boolean waitForFileDownload(String fileNameKeyword, int timeoutInSeconds) {
		String downloadPath = System.getProperty("user.home") + "/Downloads";
		File dir = new File(downloadPath);
		int elapsed = 0;

		while (elapsed < timeoutInSeconds) {
			File[] files = dir.listFiles();
			if (files != null) {
				for (File file : files) {
					// Check if file contains keyword and is fully downloaded (ignoring partial
					// .crdownload or .tmp files)
					if (file.getName().contains(fileNameKeyword) && !file.getName().endsWith(".crdownload")
							&& !file.getName().endsWith(".tmp")) {
						return true;
					}
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			elapsed++;
		}
		return false;
	}
}