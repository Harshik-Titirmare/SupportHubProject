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

	// ==================== LOCATORS ====================
	private By newTicketBtn = By.xpath("//*[@id='root']/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div/button[4]");
	private By searchBox = By.xpath("//input[contains(@placeholder,'Search subject')]");

	// Post-Creation Verification Locators
	private By activeHeaderTicketID = By.xpath("//div[contains(@class,'font-mono') or contains(text(),'TKT-')]");

	// Locator for Customer Name in the right-side Customer Info panel (where arrow
	// points)
	private By customerNameHeaderLocator = By.xpath("");

	// Filter Icon Button & Dropdowns
	private By filterIcon = By.xpath("//*[@id='root']/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div/button[1]");
	private By statusDropdownBtn = By.xpath(
			"//div[text()='STATUS']/parent::div//button | (//div[contains(@class,'USER FILTER') or text()='USER FILTER']/following::button)[2] | //*[contains(text(),'exclude_closed') or contains(text(),'Selected')]/ancestor::button");
	private By priorityDropdownBtn = By.xpath(
			"//div[text()='PRIORITY']/parent::div//button | (//div[contains(@class,'USER FILTER') or text()='USER FILTER']/following::button)[3] | //*[contains(text(),'Any Priority') or contains(text(),'Urgent')]/ancestor::button");
	// Locator for Reset View / Reset Filter button
	private By resetFilterBtn = By.xpath("//button[contains(text(),'Reset View') or contains(text(),'Reset')]");
	// Search input fields inside popovers
	private By searchStatusInput = By.xpath("//input[contains(@placeholder,'Search status')]");
	private By searchPriorityInput = By.xpath("//input[contains(@placeholder,'Search priority')]");

	// Save View Locators
	private By saveAsNewBtn = By.xpath(
			"//*[contains(text(),'Save As New') or contains(text(),'Save as new') or contains(text(),'Save View')]");
	// Strictly target the 'View name...' input field to avoid matching dropdown
	// search boxes
	private By viewNameInput = By.xpath("//input[@placeholder='View name...' or @placeholder='View name…']");
	private By saveCheckmarkBtn = By
			.xpath("//*[@id=\"root\"]/div[2]/div[1]/div[2]/main/div/div[2]/div[2]/div[2]/div/div[10]/div[1]/button");
	private By pinSidebarBtn = By.xpath(
			"//*[@id='root']/div[2]/div[1]/div[1]//button | //button[contains(@title,'Pin') or contains(@aria-label,'Pin') or .//*[contains(@class,'pin')]]");
	private By globalViewCheckbox = By.xpath(
			"//*[contains(text(),'Save as Global View')]/preceding-sibling::input | //*[contains(text(),'Save as Global View')]/parent::label");

	// Category Flow Locators
	private By setCategoryBtn = By.xpath("//button[contains(.,'Set Category')]");
	private By selectCategoryDropdownBtn = By.xpath("//span[contains(text(),'— Select Category —')]");
	private By searchCategoryInput = By.xpath("//input[contains(@placeholder,'Search category')]");
	private By selectSubcategoryDropdownBtn = By.xpath("//span[contains(text(),'— Select Subcategory —')]");

	// Status & Interaction Flow Locators
	private By statusOpenBtn = By
			.xpath("//body/div[@id='root']/div/div/div/main/div/div/div[1]/div[2]/div[1]/div[1]/div[1]/button[1]");
	private By interactionNotesInput = By.xpath("//textarea[contains(@placeholder,'Describe what happened')]");
	private By logInteractionSubmitBtn = By.xpath("//button[contains(text(),'Log Interaction')]");
	private By interactionToastMsg = By.xpath("//*[contains(text(),'Interaction logged')]");
	// Locators for Closed tab, Search box, and searched ticket ID
	private By closedTabLocator = By.xpath("//span[normalize-space()='Closed']");
	private By searchBoxLocator = By.xpath("//input[contains(@placeholder, 'Search subject, TKT#')]");

	// Locator for the header status badge (e.g., OPEN, CLOSED)
	// Updated header status locator using your provided XPath
	private By headerStatusLocator = By
			.xpath("//body/div[@id='root']/div/div/div/main/div/div/div[1]/div[2]/div[1]/div[1]/div[1]/button[1]");

	// Locator for the header priority badge (e.g., URGENT, MEDIUM, LOW, HIGH)
	private By headerPriorityLocator = By
			.xpath("//body/div[@id='root']/div/div/div/main/div/div/div/div/div/div/div/button[2]");

	// Locator definition at the top of the class
	private By ticketsTab = By.xpath(
			"//aside//a[@href='/tickets'] | //div[contains(@class, 'sidebar')]//a[@href='/tickets'] | //a[contains(@href, '/tickets')]");

	// Method to navigate to Tickets tab using the defined locator
	public void navigateToTickets() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(ticketsTab));
		tab.click();
	}

	// ==================== CONSTRUCTOR ====================
	public TicketPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	// ==================== PAGE ACTIONS ====================
	public void clickNewTicketButton() {
		action.click(newTicketBtn);
	}

	public void searchTicket(String text) {
		action.enterText(searchBox, text);
	}

	public void toggleFilterOverlay() {
		action.click(filterIcon);
	}

	// Get active header ticket ID after creation
	public String getActiveTicketIDFromHeader() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
		return wait.until(ExpectedConditions.visibilityOfElementLocated(activeHeaderTicketID)).getText().trim();
	}

	public String getCustomerNameFromPanel() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement customerElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(customerNameHeaderLocator));
			return customerElement.getText().trim();
		} catch (Exception e) {
			System.out.println("Exception in getCustomerNameFromPanel: " + e.getMessage());
			return "";
		}
	}

	// Dynamic method to verify customer name in the panel
	public boolean isCustomerNameMatching(String expectedCustomerName) {
		try {
			// Dynamic XPath using the passed customer name parameter
			String dynamicXPath = "//p[contains(text(), '" + expectedCustomerName + "')]";

			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement customerElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(dynamicXPath)));

			String actualName = customerElement.getText().trim();
			return actualName.equalsIgnoreCase(expectedCustomerName);
		} catch (Exception e) {
			System.out.println("Exception in isCustomerNameMatching: " + e.getMessage());
			return false;
		}
	}

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

	public void selectFilterPriority(String priorityName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(priorityDropdownBtn));
		dropdown.click();

		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchPriorityInput));
		searchInput.clear();
		searchInput.sendKeys(priorityName);
		searchInput.sendKeys(Keys.ENTER);

		dropdown.click();
	}

	// Get status from header view
	public String getActiveTicketStatusFromHeader() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(headerStatusLocator));
			return statusElement.getText().trim();
		} catch (Exception e) {
			System.out.println("Exception in getActiveTicketStatusFromHeader: " + e.getMessage());
			return "";
		}
	}

	// Get priority from header view
	public String getActiveTicketPriorityFromHeader() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement priorityElement = wait
					.until(ExpectedConditions.visibilityOfElementLocated(headerPriorityLocator));
			return priorityElement.getText().trim();
		} catch (Exception e) {
			System.out.println("Exception in getActiveTicketPriorityFromHeader: " + e.getMessage());
			return "";
		}
	}

	public void saveCustomFilterView(String viewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement saveBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveAsNewBtn));
		js.executeScript("arguments[0].scrollIntoView(true);", saveBtn);
		js.executeScript("arguments[0].click();", saveBtn);

		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(viewNameInput));
		input.clear();
		input.sendKeys(viewName);

		WebElement checkmark = wait.until(ExpectedConditions.elementToBeClickable(saveCheckmarkBtn));
		checkmark.click();
	}

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

	public void clickPinSidebar() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement pinBtn = wait.until(ExpectedConditions.elementToBeClickable(pinSidebarBtn));
		pinBtn.click();
	}

	public void clickCustomViewShortcut(String viewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		By customViewLocator = By.xpath("//*[contains(text(),'MY VIEWS')]/following::*[contains(text(),'" + viewName
				+ "')][1] | //*[contains(text(),'" + viewName + "')]");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		WebElement customView = wait.until(ExpectedConditions.elementToBeClickable(customViewLocator));
		customView.click();
	}

	/// Method to click on the global view shortcut under GLOBAL VIEWS dynamically
	/// based on name
	public void clickGlobalViewShortcut(String globalViewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Dynamic XPath using the globalViewName parameter to target the text span
		// inside global views section
		By globalViewLocator = By
				.xpath("//div[contains(text(),'GLOBAL VIEWS')]/following-sibling::*//span[normalize-space()='"
						+ globalViewName + "'] | //span[normalize-space()='" + globalViewName + "']");

		WebElement viewElement = wait.until(ExpectedConditions.elementToBeClickable(globalViewLocator));
		try {
			viewElement.click();
		} catch (Exception e) {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click();", viewElement);
		}
	}

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

	public void resetFilters() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			WebElement resetBtn = wait.until(ExpectedConditions.elementToBeClickable(resetFilterBtn));
			resetBtn.click();
		} catch (Exception e) {
			// Fallback: If reset button is inside overlay, toggle overlay first and then
			// reset
			try {
				toggleFilterOverlay();
				WebElement resetBtn = driver.findElement(resetFilterBtn);
				resetBtn.click();
				toggleFilterOverlay();
			} catch (Exception ex) {
				System.out.println("Could not reset filters automatically: " + ex.getMessage());
			}
		}
	}

	public void searchTicketByIdOrSubject(String ticketId) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBox));
		searchInput.clear();
		searchInput.sendKeys(ticketId);
	}

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

	public void clearSearchBox() {
		try {
			// Option A: Navigate back to clean tickets URL where no search query exists
			driver.get("https://demo.wevyn.ai/tickets");

			// Short wait for the page to load fresh
			Thread.sleep(1500);
		} catch (Exception e) {
			System.out.println("Could not clear search via URL: " + e.getMessage());
		}
	}

	public void saveGlobalFilterView(String viewName) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		WebElement saveAsBtn = wait.until(ExpectedConditions.presenceOfElementLocated(saveAsNewBtn));
		js.executeScript("arguments[0].scrollIntoView(true);", saveAsBtn);
		wait.until(ExpectedConditions.elementToBeClickable(saveAsBtn)).click();

		WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(viewNameInput));
		nameInput.clear();
		nameInput.sendKeys(viewName);

		try {
			WebElement globalCheckbox = driver.findElement(globalViewCheckbox);
			if (!globalCheckbox.isSelected()) {
				globalCheckbox.click();
			}
		} catch (Exception e) {
			WebElement globalLabel = driver.findElement(By.xpath("//*[contains(text(),'Save as Global View')]"));
			js.executeScript("arguments[0].click();", globalLabel);
		}

		WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(saveCheckmarkBtn));
		confirmBtn.click();
	}

	public boolean verifyGlobalViewToastMessage(String viewName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By toastMsg = By.xpath(
					"//*[contains(text(),'Global view \"" + viewName + "\" saved') or contains(text(),'saved')]");
			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMsg));
			return toastElement.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void clickFirstTicketInList() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Updated robust locator targeting the ticket card using role='button' and TKT-
		// text
		By ticketLocator = By.xpath("(//div[@role='button' and descendant::span[contains(text(),'TKT-')]])[1]");

		try {
			WebElement firstTicket = wait.until(ExpectedConditions.elementToBeClickable(ticketLocator));
			try {
				firstTicket.click();
			} catch (Exception e) {
				js.executeScript("arguments[0].click();", firstTicket);
			}
		} catch (Exception e) {
			throw new RuntimeException("First ticket in the list could not be found! Reason: " + e.getMessage());
		}
	}

	public void setTicketCategoryFlow(String category, String subcategory, String subSubcategory) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		try {
			WebElement catBtn = wait.until(ExpectedConditions.elementToBeClickable(setCategoryBtn));
			catBtn.click();
		} catch (Exception ignored) {
		}

		// --- STEP 1: Select Category ---
		WebElement selectCatBtn = wait.until(ExpectedConditions.elementToBeClickable(selectCategoryDropdownBtn));
		selectCatBtn.click();

		WebElement catSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(searchCategoryInput));
		catSearch.clear();
		catSearch.sendKeys(category);
		catSearch.sendKeys(Keys.ENTER);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// --- STEP 2: Select Subcategory ---
		WebElement selectSubBtn = wait.until(ExpectedConditions.elementToBeClickable(selectSubcategoryDropdownBtn));
		js.executeScript("arguments[0].click();", selectSubBtn);

		By subCategoryOption = By
				.xpath("//div[contains(@class,'cursor-pointer') or @role='option']//div[normalize-space()='"
						+ subcategory + "'] | //div[normalize-space()='" + subcategory + "']");
		WebElement subOpt = wait.until(ExpectedConditions.elementToBeClickable(subCategoryOption));
		js.executeScript("arguments[0].click();", subOpt);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		// --- STEP 3: Select Sub-subcategory / Final option ---
		try {
			WebElement selectSubSubBtn = wait.until(ExpectedConditions.elementToBeClickable(
					By.xpath("//span[contains(text(),'— Select Subcategory —') or contains(@class,'placeholder')]")));
			js.executeScript("arguments[0].click();", selectSubSubBtn);
		} catch (Exception ignored) {
		}

		try {
			Thread.sleep(800);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		WebElement subSubSearch = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
				"(//input[contains(@placeholder,'Search subcategory') or contains(@placeholder,'Search')])[last()]")));
		subSubSearch.clear();
		subSubSearch.sendKeys(subSubcategory);
		subSubSearch.sendKeys(Keys.ENTER);

		try {
			Thread.sleep(800);
			By subSubOption = By
					.xpath("//div[contains(@class,'cursor-pointer') or @role='option']//div[normalize-space()='"
							+ subSubcategory + "'] | //div[normalize-space()='" + subSubcategory + "']");
			WebElement subSubOpt = wait.until(ExpectedConditions.elementToBeClickable(subSubOption));
			js.executeScript("arguments[0].click();", subSubOpt);
		} catch (Exception ignored) {
		}
	}

	public void changeTicketStatusToClosed(String targetStatus) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Click status dropdown instantly without sleep delay
		WebElement statusBtn = wait.until(ExpectedConditions.elementToBeClickable(statusOpenBtn));
		try {
			statusBtn.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", statusBtn);
		}

		// Select target status instantly using robust combined XPath
		By statusOption = By.xpath("//div[contains(@class,'cursor-pointer') or @role='option']//div[normalize-space()='"
				+ targetStatus + "'] | " + "//div[normalize-space()='" + targetStatus + "'] | "
				+ "//*[contains(@class,'menu')]//*[normalize-space()='" + targetStatus + "']");

		WebElement option = wait.until(ExpectedConditions.elementToBeClickable(statusOption));
		try {
			option.click();
		} catch (Exception e) {
			js.executeScript("arguments[0].click();", option);
		}
	}

	public void fillInteractionNotesAndLog(String noteText) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

		WebElement notesField = wait.until(ExpectedConditions.visibilityOfElementLocated(interactionNotesInput));
		notesField.clear();
		notesField.sendKeys(noteText);

		WebElement logBtn = wait.until(ExpectedConditions.elementToBeClickable(logInteractionSubmitBtn));
		logBtn.click();
	}

	public boolean verifyInteractionLoggedToast() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(interactionToastMsg));
			return toast.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	public void clickClosedTab() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement closedTab = wait.until(ExpectedConditions.elementToBeClickable(closedTabLocator));

			// Scroll into view and click using JS executor to avoid interception
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].scrollIntoView(true);", closedTab);
			js.executeScript("arguments[0].click();", closedTab);

			// Small pause to let the Closed view load completely
			Thread.sleep(2000);
		} catch (Exception e) {
			System.out.println("Exception in clickClosedTab: " + e.getMessage());
		}
	}

	// 2. Type/Search the copied Ticket ID in the search box
	public void searchTicketByID(String ticketId) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(searchBoxLocator));
		searchBox.clear();
		searchBox.sendKeys(ticketId);

		// Optional: 1-second pause or small wait for search results to filter out
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
		}
	}

	// 3. Verify that the ticket appears in the filtered list and matches the ID
	public boolean verifySearchedTicketID(String expectedTicketId) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement ticketElement = wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + expectedTicketId + "')]")));
			String foundText = ticketElement.getText().trim();
			return foundText.contains(expectedTicketId);
		} catch (Exception e) {
			System.out.println("Exception in verifySearchedTicketID: " + e.getMessage());
			return false;
		}
	}

	// 4. Verify that the ticket status in the list is strictly 'CLOSED'
	public boolean isTicketStatusClosedInList() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement statusElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
					By.xpath("//div[contains(text(),'CLOSED') or contains(text(),'Closed')]")));

			String statusText = statusElement.getText().trim();
			return statusText.equalsIgnoreCase("CLOSED");
		} catch (Exception e) {
			System.out.println("Exception in verifying closed status: " + e.getMessage());
			return false;
		}
	}
}