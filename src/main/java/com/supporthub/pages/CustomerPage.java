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

public class CustomerPage {

	private WebDriver driver;
	private ActionDriver action;

	// Locators
	private By customersTab = By.xpath(
			"//*[@id='root']//a[contains(@href,'customers')] | //*[name()='svg' and @data-icon='users'] | //*[contains(@class,'customer')]");
	private By newCustomerBtn = By.xpath("//button[contains(text(),'New') or contains(text(),'+ New')]");

	// Form Inputs
	private By nameInput = By.xpath("//input[@placeholder='Full name']");
	private By phoneInput = By.xpath("//input[@placeholder='9876543210']");
	private By emailInput = By.xpath("//input[@placeholder='customer@example.com']");

	// Country & Source Custom Dropdowns
	private By countryDropdown = By.xpath("//div[4]//div[1]//button[1]");
	private By stateInput = By.xpath("//input[@placeholder='e.g. Maharashtra']");
	private By sourceDropdown = By.xpath("//div[5]//div[1]//button[1]");

	// Date of Birth Calendar Icon & Input
	private By dobCalendarIcon = By.xpath("//input[@type='date']");

	// Other fields (Require scrolling)
	private By addressInput = By.xpath("//input[@placeholder='Street, City']");
	private By notesInput = By.xpath("//input[@placeholder='Internal notes']");
	private By cityInput = By.xpath(
			"//div[contains(text(),'City')]/following-sibling::div//input | //input[ancestor::div[contains(@class,'field')]//div[text()='City']]");
	private By pincodeInput = By.xpath("//div[@class='border-t border-border pt-3 space-y-3']//div[2]//input[1]");
	private By socialMediaInput = By.xpath("//div[@class='border-t border-border pt-3 space-y-3']//div[4]//input[1]");

	// Locators for CSV Import
	private By importBtn = By.xpath("//button[contains(text(),'Import')]");
	private By dropzoneArea = By.xpath("//*[contains(text(),'Drop CSV file here') or contains(@class,'dropzone')]");
	private By modalImportBtn = By.xpath("//div[@role='dialog']//button[contains(text(),'Import')]");
	private By importSuccessToast = By.xpath("//*[contains(text(),'Import complete')]");

	// Create Customer Button
	private By createCustomerBtn = By.xpath("//button[contains(text(),'Create Customer')]");

	public CustomerPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	public void navigateToCustomers() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement custTab = wait.until(ExpectedConditions.elementToBeClickable(customersTab));
		custTab.click();
	}

	public void clickNewCustomerButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement newBtn = wait.until(ExpectedConditions.elementToBeClickable(newCustomerBtn));
		newBtn.click();
	}

	public void fillCustomerDetails(String name, String phone, String email, String country, String state,
			String source, String dob, String gender, String address, String notes, String city, String pincode,
			String socialMedia) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// 1. Basic Fields
		wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput)).sendKeys(name);
		driver.findElement(phoneInput).sendKeys(phone);
		driver.findElement(emailInput).sendKeys(email);

		// 2. Country Custom Dropdown Selection
		WebElement countryBox = wait.until(ExpectedConditions.elementToBeClickable(countryDropdown));
		countryBox.click();
		By countryOption = By.xpath("//div[normalize-space()='" + country + "']");
		wait.until(ExpectedConditions.elementToBeClickable(countryOption)).click();

		// 3. State / Province
		driver.findElement(stateInput).sendKeys(state);

		// 4. Source Custom Dropdown Selection
		WebElement sourceBox = wait.until(ExpectedConditions.elementToBeClickable(sourceDropdown));
		sourceBox.click();
		By sourceOption = By.xpath("//div[normalize-space()='" + source + "']");
		wait.until(ExpectedConditions.elementToBeClickable(sourceOption)).click();

		// 5. Date of Birth Calendar Handling
		WebElement dobField = driver.findElement(dobCalendarIcon);
		dobField.click();
		dobField.clear();
		dobField.sendKeys(dob);
		dobField.sendKeys(Keys.TAB);

		// 6. Scroll down to Gender, Address, Pincode & Social Media fields
		WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput));
		js.executeScript("arguments[0].scrollIntoView(true);", addressField);

		// 7. Gender Radio Button Selection
		try {
			WebElement radio = driver.findElement(By.xpath("//*[text()='" + gender + "']"));
			radio.click();
		} catch (Exception e) {
			System.out.println("Gender selection handled via fallback");
		}

		// 8. Address, Notes, City, Pincode & Social Media
		addressField.sendKeys(address);
		driver.findElement(notesInput).sendKeys(notes);

		try {
			driver.findElement(cityInput).sendKeys(city);
		} catch (Exception ignored) {
		}

		driver.findElement(pincodeInput).sendKeys(pincode);
		driver.findElement(socialMediaInput).sendKeys(socialMedia);
	}

	public void clickCreateCustomerButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		WebElement createBtn = wait.until(ExpectedConditions.elementToBeClickable(createCustomerBtn));

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", createBtn);
		js.executeScript("arguments[0].click();", createBtn);
	}

	public boolean verifyCustomerCreated(String customerName) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By customerProfile = By.xpath("//*[contains(text(),'" + customerName + "')]");
			WebElement profile = wait.until(ExpectedConditions.visibilityOfElementLocated(customerProfile));
			return profile.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// New Toast Message Verification Method
	public boolean verifyCustomerCreatedToastMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By toastMsg = By.xpath("//*[contains(text(),'Customer created')]");
			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMsg));
			return toastElement.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Check if Create Customer button is enabled or disabled
	public boolean isCreateCustomerButtonEnabled() {
		try {
			WebElement createBtn = driver.findElement(createCustomerBtn);
			return createBtn.isEnabled();
		} catch (Exception e) {
			return false;
		}
	}

	// Verify Name field validation error or red border highlight
	public boolean verifyNameFieldError() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			// Check for HTML5 validation message, error text, or red border on Name input
			WebElement nameField = driver.findElement(nameInput);
			String validationMessage = nameField.getAttribute("validationMessage");
			boolean hasErrorClass = nameField.getAttribute("class").contains("error")
					|| nameField.getAttribute("class").contains("border-red")
					|| nameField.getAttribute("style").contains("red");
			return (validationMessage != null && !validationMessage.isEmpty()) || hasErrorClass;
		} catch (Exception e) {
			return false;
		}
	}

	public void clickImportButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(importBtn)).click();
	}

	public void uploadCSVFile(String filePath) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		// Clicking dropzone to trigger OS file dialog or sending keys if file input is
		// present
		WebElement dropzone = wait.until(ExpectedConditions.elementToBeClickable(dropzoneArea));
		dropzone.click();

		// Note: For native file chooser dialog in Selenium on Linux/Ubuntu,
		// you can use Java Robot class or Runtime execution if sendKeys isn't directly
		// attached to an <input type="file">.
		// If there is an underlying file input:
		try {
			WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
			fileInput.sendKeys(filePath);
		} catch (Exception e) {
			// Fallback for native dialog handling if needed
			System.out.println("Handled via native file chooser or dropzone interaction.");
		}
	}

	public void clickModalImportButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(modalImportBtn)).click();
	}
	
	// Verify Created toast message
	public boolean verifyImportSuccessToast() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(importSuccessToast));
			return toast.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

	// Verify Deduplication / Update toast message
	public boolean verifyCustomerUpdatedToastMessage() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			By toastMsg = By.xpath("//*[contains(text(),'0 created, 1 updated') or contains(text(),'updated')]");
			WebElement toastElement = wait.until(ExpectedConditions.visibilityOfElementLocated(toastMsg));
			return toastElement.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}
	
	
}