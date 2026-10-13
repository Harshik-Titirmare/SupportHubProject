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

	// Locators for navigation and elements
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

	// Other fields requiring scroll interaction
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

		// Helper method lambda or logic to clear and type safely using JS events
		// 1. Name Field (Can be blank for negative testing)
		WebElement nameFld = wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				nameFld);
		if (name != null && !name.trim().isEmpty()) {
			nameFld.sendKeys(name);
		}

		// 2. Phone Field
		WebElement phoneFld = driver.findElement(phoneInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				phoneFld);
		if (phone != null && !phone.trim().isEmpty()) {
			phoneFld.sendKeys(phone);
		}

		// 3. Email Field
		WebElement emailFld = driver.findElement(emailInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				emailFld);
		if (email != null && !email.trim().isEmpty()) {
			emailFld.sendKeys(email);
		}

		// 4. Select country from custom dropdown
		WebElement countryBox = wait.until(ExpectedConditions.elementToBeClickable(countryDropdown));
		countryBox.click();
		By countryOption = By.xpath("//div[normalize-space()='" + country + "']");
		wait.until(ExpectedConditions.elementToBeClickable(countryOption)).click();

		// 5. Enter state or province
		WebElement stateFld = driver.findElement(stateInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				stateFld);
		if (state != null && !state.trim().isEmpty()) {
			stateFld.sendKeys(state);
		}

		// 6. Select source from custom dropdown
		WebElement sourceBox = wait.until(ExpectedConditions.elementToBeClickable(sourceDropdown));
		sourceBox.click();
		By sourceOption = By.xpath("//div[normalize-space()='" + source + "']");
		wait.until(ExpectedConditions.elementToBeClickable(sourceOption)).click();

		// 7. Handle Date of Birth input field
		WebElement dobField = driver.findElement(dobCalendarIcon);
		dobField.click();
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				dobField);
		if (dob != null && !dob.trim().isEmpty()) {
			dobField.sendKeys(dob);
			dobField.sendKeys(Keys.TAB);
		}

		// 8. Scroll down to view secondary fields
		WebElement addressField = wait.until(ExpectedConditions.visibilityOfElementLocated(addressInput));
		js.executeScript("arguments[0].scrollIntoView(true);", addressField);

		// 9. Select gender radio option safely
		try {
			WebElement radio = driver.findElement(By.xpath("//*[text()='" + gender + "']"));
			radio.click();
		} catch (Exception e) {
			System.out.println("Gender selection handled via fallback.");
		}

		// 10. Fill secondary fields with strict JS reset
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				addressField);
		if (address != null && !address.trim().isEmpty()) {
			addressField.sendKeys(address);
		}

		WebElement notesFld = driver.findElement(notesInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				notesFld);
		if (notes != null && !notes.trim().isEmpty()) {
			notesFld.sendKeys(notes);
		}

		try {
			WebElement cityFld = driver.findElement(cityInput);
			js.executeScript(
					"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
					cityFld);
			if (city != null && !city.trim().isEmpty()) {
				cityFld.sendKeys(city);
			}
		} catch (Exception ignored) {
		}

		WebElement pincodeFld = driver.findElement(pincodeInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				pincodeFld);
		if (pincode != null && !pincode.trim().isEmpty()) {
			pincodeFld.sendKeys(pincode);
		}

		WebElement socialFld = driver.findElement(socialMediaInput);
		js.executeScript(
				"arguments[0].value=''; arguments[0].dispatchEvent(new Event('input', { bubbles: true })); arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
				socialFld);
		if (socialMedia != null && !socialMedia.trim().isEmpty()) {
			socialFld.sendKeys(socialMedia);
		}
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

	public boolean isCreateCustomerButtonEnabled() {
		try {
			WebElement createBtn = driver.findElement(createCustomerBtn);
			return createBtn.isEnabled();
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
		try {
			WebElement fileInput = driver.findElement(By.xpath("//input[@type='file']"));
			fileInput.sendKeys(filePath);
		} catch (Exception e) {
			try {
				WebElement fileInput = wait
						.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='file']")));
				JavascriptExecutor js = (JavascriptExecutor) driver;
				js.executeScript("arguments[0].style.display='block'; arguments[0].style.visibility='visible';",
						fileInput);
				fileInput.sendKeys(filePath);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void clickModalImportButton() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.elementToBeClickable(modalImportBtn)).click();
	}

	public boolean verifyImportSuccessToast() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement toast = wait.until(ExpectedConditions.visibilityOfElementLocated(importSuccessToast));
			return toast.isDisplayed();
		} catch (Exception e) {
			return false;
		}
	}

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