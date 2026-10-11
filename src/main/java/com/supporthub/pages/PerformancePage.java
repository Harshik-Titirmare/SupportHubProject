package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class PerformancePage {

	private WebDriver driver;
	private ActionDriver action;

	// --- All Locators Cleanly Declared at the Top ---
	private By performanceNavTab = By.xpath("//a[contains(@href,'/performance') or @title='Performance']");
	private By agentSearchInput = By.xpath("//input[contains(@placeholder,'Search') or @type='search' or contains(@class,'search')]");
	
	// Team & Filter Locators
	private By teamsDropdownBtn = By.xpath("//*[normalize-space()='All Teams']");
	private By teamDropdownSearchInput = By.xpath("//div[contains(@class,'dropdown') or contains(@class,'menu') or contains(@class, 'popover')]//input[@placeholder='Search...'] | //div[@role='listbox']//input[@placeholder='Search...'] | //input[@placeholder='Search...'][ancestor::div[contains(@class,'absolute')]]");
	
	// Date Dropdown Locators
	private By dateDropdownBtn = By.xpath("//button[contains(.,'Today') or contains(.,'This Month')]");
	
	// Export Button Locator
	private By exportExcelBtn = By.xpath("//button[contains(text(),'Export Excel')]");
	
	// Message Locator
	private By noDataMsgLocator = By.xpath("//*[contains(text(),'No data for selected filter')]");

	public PerformancePage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	public void navigateToPerformance() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement navBtn = wait.until(ExpectedConditions.elementToBeClickable(performanceNavTab));
		navBtn.click();
	}

	public void searchAgent(String agentName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(agentSearchInput));
		searchBox.clear();
		searchBox.sendKeys(agentName);
	}

	public boolean isAgentRowDisplayed(String expectedFullName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By dynamicRowLocator = By.xpath("//*[contains(text(),'" + expectedFullName + "')]");
			WebElement row = wait.until(ExpectedConditions.visibilityOfElementLocated(dynamicRowLocator));
			return row.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Verify specific "No data for selected filter" message
	public boolean verifyNoDataMessageDisplayed() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement msgElement = wait.until(ExpectedConditions.visibilityOfElementLocated(noDataMsgLocator));
			return msgElement.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void selectTeam(String teamName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		// Click 'All Teams' dropdown
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(teamsDropdownBtn));
		dropdown.click();

		// Type in team popup search box
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(teamDropdownSearchInput));
		searchBox.clear();
		searchBox.sendKeys(teamName);

		// Select specific team option dynamically
		By teamOptionLocator = By.xpath("//div[contains(@class,'option') and contains(text(),'" + teamName + "')] | //span[contains(text(),'" + teamName + "')] | //div[text()='" + teamName + "']");
		WebElement option = wait.until(ExpectedConditions.elementToBeClickable(teamOptionLocator));
		option.click();
	}

	public void selectDateFilter(String dateFilterText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		
		// Click Date dropdown
		WebElement dateBtn = wait.until(ExpectedConditions.elementToBeClickable(dateDropdownBtn));
		dateBtn.click();

		// Select Date Filter option dynamically (e.g., 'This Month')
		By dateOptionLocator = By.xpath("//div[contains(text(),'" + dateFilterText + "') or span[contains(text(),'" + dateFilterText + "')]]");
		WebElement filterOption = wait.until(ExpectedConditions.elementToBeClickable(dateOptionLocator));
		filterOption.click();
	}

	public void clickExportExcelButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement exportBtn = wait.until(ExpectedConditions.elementToBeClickable(exportExcelBtn));
		exportBtn.click();
	}
}