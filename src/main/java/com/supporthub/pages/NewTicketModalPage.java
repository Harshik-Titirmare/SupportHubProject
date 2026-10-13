package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class NewTicketModalPage {

	private WebDriver driver;
	private ActionDriver action;

	// Locators
	private By customerInput = By
			.xpath("//input[contains(@placeholder,'Search by name') or contains(@placeholder,'Customer')]");
	private By subjectInput = By.xpath(
			"//input[contains(@placeholder,'What is this ticket about') or contains(@placeholder,'Subject') or @name='subject']");
	private By createTicketBtn = By.xpath("//button[@type='submit' or contains(text(),'Create Ticket')]");

	// Toast Error Locators
	private By customerRequiredToast = By.xpath("//*[contains(text(),'Customer is required')]");
	private By subjectRequiredToast = By.xpath("//*[contains(text(),'Subject is required')]");
	private By ticketCreatedSuccessToast = By.xpath("//*[contains(text(),'Ticket created')]");

	public NewTicketModalPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	public void selectCustomerFromDropdown(String customerName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement custInput = wait.until(ExpectedConditions.elementToBeClickable(customerInput));
		custInput.click();
		custInput.clear();
		custInput.sendKeys(customerName);

		By customerOption = By.xpath("//div[contains(@class,'cursor-pointer')]//p[contains(text(),'" + customerName
				+ "')] | //*[contains(@class,'font-medium') and contains(text(),'" + customerName + "')]");
		WebElement optionElement = wait.until(ExpectedConditions.presenceOfElementLocated(customerOption));

		Actions actions = new Actions(driver);
		actions.moveToElement(optionElement).click().build().perform();

		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// Method to type in Customer field without selecting dropdown item
	public void enterCustomerWithoutSelecting(String customerName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement custInput = wait.until(ExpectedConditions.elementToBeClickable(customerInput));
		custInput.click();
		custInput.clear();
		custInput.sendKeys(customerName);
	}

	public void enterSubject(String subject) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(subjectInput));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].focus();", element);

		element.clear();
		element.sendKeys(subject);
	}

	public void clickCreateTicket() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(createTicketBtn));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click();", btn);
		System.out.println("Clicked on Create Ticket button.");
	}

	public boolean isCustomerRequiredErrorDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			return wait.until(ExpectedConditions.visibilityOfElementLocated(customerRequiredToast)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Check if 'Subject is required' toast error is displayed
	public boolean isSubjectRequiredErrorDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			return wait.until(ExpectedConditions.visibilityOfElementLocated(subjectRequiredToast)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isTicketCreatedToastDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			return wait.until(ExpectedConditions.visibilityOfElementLocated(ticketCreatedSuccessToast)).isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Locator for the Cancel button in New Ticket Modal
		private By cancelModalBtn = By.xpath("//button[normalize-space()='Cancel']");
		
	// Method to click Cancel button and close the modal
	public void clickCancelButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement cancelBtn = wait.until(ExpectedConditions.elementToBeClickable(cancelModalBtn));
		cancelBtn.click();
	}
}