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

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		System.out.println("Webdriver instance is created.");
	}

	// Method To Click Element
	public void click(By by) {
	    try {
	        waitForElementToBeClickable(by);
	        driver.findElement(by).click();
	    } catch (Exception e) {
	        System.out.println("Unable to Click the Element: " + e.getMessage());
	        throw e; 
	    }
	}

	// Method to Enter Text into an input Field --Avoid CODE Duplication --fix the
	// mutiple call
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

	// Method to get text from an Input Field
	public String getText(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).getText();
		} catch (Exception e) {
			System.out.println("Unable to get The Text: " + e.getMessage());
			return "";
		}
	}

	// Method To Compare two Text -- change the return type
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
			String actualText = driver.findElement(by).getText();
			if (expectedText.equals(actualText)) {
				System.out.println("The text are Matching: " + actualText + " Equals" + expectedText);
				return true;
			} else {
				System.out.println("The text are Not Matching: " + actualText + " Not Equals" + expectedText);
			}
		} catch (Exception e) {
			System.out.println("Unable to Compare the text: " + e.getMessage());
			return false;
		}
		return false;
	}

	/*
	 * // Method to Check if an element is displayed public boolean isDisplayed(By
	 * by) { try { waitForElementToBeVisible(by); boolean isDisplayed =
	 * driver.findElement(by).isDisplayed(); if (isDisplayed) {
	 * System.out.println("The Element is visible"); return isDisplayed; } else {
	 * return isDisplayed; } } catch (Exception e) {
	 * System.out.println("Element is not Displayed: " + e.getMessage()); return
	 * false; } }
	 *
	 */

	// Simplified the method and remove redundant conditions
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisible(by);
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			System.out.println("Element is not Displayed: " + e.getMessage());
			return false;
		}
	}

	// wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			System.out.println("Page is Loaded successfully.");
		} catch (Exception e) {
			System.out.println("Page did not load within " + timeOutInSec + " Seconds. Exception: " + e.getMessage());
		}
	}

	// Scroll to an Element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0],scrollIntoView(true);", element);
		} catch (Exception e) {
			System.out.println("Unable to locate Element: " + e.getMessage());
		}
	}
	
	// Method to Handle Alert Popup if present
	public void handleAlertIfPresent() {
	    try {
	        WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(3));
	        alertWait.until(ExpectedConditions.alertIsPresent());
	        driver.switchTo().alert().accept(); // Clicks 'OK' on alert
	        System.out.println("Alert popup accepted successfully.");
	    } catch (Exception e) {
	        System.out.println("No alert popup appeared, proceeding normally.");
	    }
	}
	
	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			System.out.println("Element is not clickable: " + e.getMessage());
		}
	}

	// wait for Element to be Visible
	private void waitForElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {
			System.out.println("Element is not Visible: " + e.getMessage());
		}
	}
	
	// Generic method to check if a file is downloaded in the local Downloads folder
    public boolean waitForFileDownload(String fileNameKeyword, int timeoutInSeconds) {
        String downloadPath = System.getProperty("user.home") + "/Downloads";
        File dir = new File(downloadPath);
        int elapsed = 0;

        while (elapsed < timeoutInSeconds) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Checks if file contains keyword and is fully downloaded (not partial .crdownload)
                    if (file.getName().contains(fileNameKeyword) && !file.getName().endsWith(".crdownload") && !file.getName().endsWith(".tmp")) {
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
