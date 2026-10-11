package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class TicketPage {

	private WebDriver driver;
	private ActionDriver action;

	// Locators
	private By newTicketBtn = By.xpath("//*[@id='root']/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div/button[3]");
	private By searchBox = By.xpath("//input[contains(@placeholder,'Search subject')]");

	// Filter Icon Button
	private By filterIcon = By.xpath("//*[@id='root']/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div/button[1]");

	// Dropdown Buttons in USER FILTER Panel
	private By statusDropdownBtn = By.xpath(
			"//div[text()='STATUS']/parent::div//button | (//div[contains(@class,'USER FILTER') or text()='USER FILTER']/following::button)[2] | //*[contains(text(),'exclude_closed') or contains(text(),'Selected')]/ancestor::button");
	private By priorityDropdownBtn = By.xpath(
			"//div[text()='PRIORITY']/parent::div//button | (//div[contains(@class,'USER FILTER') or text()='USER FILTER']/following::button)[3] | //*[contains(text(),'Any Priority') or contains(text(),'Urgent')]/ancestor::button");

	// Search input fields inside popover menus
	private By searchStatusInput = By.xpath("//input[contains(@placeholder,'Search status')]");
	private By searchPriorityInput = By.xpath("//input[contains(@placeholder,'Search priority')]");

	// TC_TKT_004 Exact Working Locators
	private By saveAsNewBtn = By.xpath(
			"//*[contains(text(),'Save As New') or contains(text(),'Save as new') or contains(text(),'Save View')]");
	private By viewNameInput = By.xpath(
			"//input[contains(@placeholder,'View name') or contains(@placeholder,'Name') or contains(@placeholder,'Enter view name')] | //div[contains(@class,'popover')]//input");

	// Tere bataye hue Exact Working XPaths
	private By saveCheckmarkBtn = By
			.xpath("//*[@id=\"root\"]/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div[2]/div/div[10]/div[1]/button");
	private By pinSidebarBtn = By.xpath(
			"//*[@id='root']/div[2]/div[1]/div[1]//button | //button[contains(@title,'Pin') or contains(@aria-label,'Pin') or .//*[contains(@class,'pin')]]");

	public TicketPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	public void clickNewTicketButton() {
		action.click(newTicketBtn);
	}

	public void searchTicket(String text) {
		action.enterText(searchBox, text);
	}

	public void toggleFilterOverlay() {
		action.click(filterIcon);
	}

	// Select Status Checkbox
	public void selectFilterStatus(String statusName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(statusDropdownBtn));
		dropdown.click();

		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchStatusInput));
		searchInput.clear();
		searchInput.sendKeys(statusName);

		By filteredItem = By.xpath("//*[contains(text(),'" + statusName + "')]");
		wait.until(ExpectedConditions.presenceOfElementLocated(filteredItem));

		searchInput.sendKeys(Keys.ARROW_DOWN);
		searchInput.sendKeys(Keys.ENTER);

		dropdown.click();
	}

	// Select Priority Checkbox
	public void selectFilterPriority(String priorityName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(priorityDropdownBtn));
		dropdown.click();

		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchPriorityInput));
		searchInput.clear();
		searchInput.sendKeys(priorityName);

		By filteredItem = By.xpath("//*[contains(text(),'" + priorityName + "')]");
		wait.until(ExpectedConditions.presenceOfElementLocated(filteredItem));

		searchInput.sendKeys(Keys.ARROW_DOWN);
		searchInput.sendKeys(Keys.ENTER);

		dropdown.click();
	}

	// Save Custom Filter View
	public void saveCustomFilterView(String viewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveAsNewBtn));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
		js.executeScript("arguments[0].click();", saveBtn);

		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(viewNameInput));
		input.clear();
		input.sendKeys(viewName);

		// Click exact checkmark button
		WebElement checkmark = wait.until(ExpectedConditions.elementToBeClickable(saveCheckmarkBtn));
		checkmark.click();
	}

	// Toast Message Verification Method
	public boolean verifyToastMessageDisplayed(String viewName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(8));
			By dynamicToast = By.xpath("//*[contains(text(),'View \"" + viewName
					+ "\" saved personally') or contains(text(),'saved personally')]");
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicToast));
			return toast.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Click Pin Sidebar using exact locator
	public void clickPinSidebar() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement pinBtn = wait.until(ExpectedConditions.elementToBeClickable(pinSidebarBtn));
		pinBtn.click();
	}

	// Click Custom Shortcut under MY VIEWS
	public void clickCustomViewShortcut(String viewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By customViewLocator = By.xpath("//*[contains(text(),'MY VIEWS')]/following::*[contains(text(),'" + viewName
				+ "')][1] | //*[contains(text(),'" + viewName + "')]");
		try {
			System.out.println("Waiting 5 seconds after clicking save checkmark...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		WebElement customView = wait.until(ExpectedConditions.elementToBeClickable(customViewLocator));
		customView.click();

	}

	// Verify "No tickets found" message is displayed
	public boolean verifyNoTicketsFoundMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By noTicketsMsg = By.xpath("//*[contains(text(),'No tickets found')]");
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(noTicketsMsg));
			return element.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	// Search ticket by ID or Subject
    public void searchTicketByIdOrSubject(String ticketId) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
        searchInput.clear();
        searchInput.sendKeys(ticketId);
    }

    // Verify filtered ticket is displayed correctly
    public boolean verifyTicketDisplayedInList(String ticketId) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By filteredTicket = By.xpath("//*[contains(text(),'" + ticketId + "')]");
            WebElement ticketElement = wait.until(ExpectedConditions.visibilityOfElementLocated(filteredTicket));
            return ticketElement.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}