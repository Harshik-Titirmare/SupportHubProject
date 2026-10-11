package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class ReminderPage {

	private WebDriver driver;
	private ActionDriver action;

	// Locators for Tickets & Reminders
	private By firstTicket = By.xpath("(//div[contains(@class,'cursor-pointer')])[1]");
	private By reminderBtn = By.xpath("//span[@class='hidden sm:inline']");
	private By reminderNoteInput = By.xpath("//input[contains(@placeholder,'Follow up')]");
	private By reminderDateInput = By.xpath("//input[@type='date']");
	private By reminderTimeInput = By.xpath("//input[@type='time']");
	private By setReminderSubmitBtn = By.xpath("//button[contains(text(),'Set Reminder')]");
	private By reminderToastMsg = By.xpath("//*[contains(text(),'Reminder set')]");

	public ReminderPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	public void clickFirstTicket() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement ticket = wait.until(ExpectedConditions.elementToBeClickable(firstTicket));
		ticket.click();
	}

	public void clickReminderButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(reminderBtn)).click();
	}

	public void fillReminderDetails(String note, String date, String time) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Enter Reminder Note
		WebElement noteField = wait.until(ExpectedConditions.visibilityOfElementLocated(reminderNoteInput));
		noteField.clear();
		noteField.sendKeys(note);

		// Enter Date
		WebElement dateField = driver.findElement(reminderDateInput);
		dateField.sendKeys(date);

		// Enter Time
		WebElement timeField = driver.findElement(reminderTimeInput);
		timeField.sendKeys(time);
	}

	public void clickSubmitSetReminder() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(setReminderSubmitBtn)).click();
	}

	public boolean verifyReminderToast() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(reminderToastMsg));
			return toast.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Check if Set Reminder button is enabled or disabled
	public boolean isSetReminderButtonEnabled() {
		try {
			WebElement submitBtn = driver.findElement(setReminderSubmitBtn);
			return submitBtn.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}
}