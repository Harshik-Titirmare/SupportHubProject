package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class LoginPage {

	private WebDriver driver;
	private ActionDriver action;

	private By changeWorkspaceLink = By.xpath("//button[5]");
	private By workspaceInput = By
			.xpath("//input[contains(@placeholder, 'demo') or contains(@placeholder, 'workspace')]");
	private By continueBtn = By.xpath("//button[contains(text(), 'Continue')]");
	private By signInHeading = By.xpath("//h2[contains(., 'Sign in')]");
	private By emailInput = By.xpath("//input[@type='email' or contains(@placeholder,'company.com')]");
	private By passwordInput = By.xpath("//input[@type='password']");
	private By signInBtn = By.xpath("//button[normalize-space()='Sign in']");
	private By errorMessageToast = By
			.xpath("//*[contains(text(), 'Login failed') or contains(text(), 'Invalid credentials')]");

	public LoginPage(WebDriver driver, ActionDriver action) {
		this.driver = driver;
		this.action = action;
	}

	// Method to handle workspace entry by clearing pre-filled text and typing
	// workspace slug
	public void enterWorkspaceAndContinue(String workspace) {
		try {
			// Check if 'Change workspace' link is present and click it if already on
			// credentials screen
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			WebElement changeLink = wait.until(ExpectedConditions.elementToBeClickable(changeWorkspaceLink));
			changeLink.click();
		} catch (Exception e) {
			// If already on workspace screen, proceed directly
			System.out.println("Already on workspace screen, proceeding further.");
		}

		// Wait for workspace input field to be visible
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement wsField = wait.until(ExpectedConditions.visibilityOfElementLocated(workspaceInput));

		// Clear pre-filled text and enter workspace slug
		wsField.clear();
		wsField.sendKeys(workspace);

		// Click on Continue button
		action.click(continueBtn);
	}

	// Method to get the sign-in heading text for workspace verification
	public String getSignInHeadingText() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(signInHeading));
		return heading.getText();
	}

	public TicketPage login(String email, String password) {
		action.enterText(emailInput, email);
		action.enterText(passwordInput, password);
		action.click(signInBtn);

		// Safe Alert Handling
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
			wait.until(ExpectedConditions.alertIsPresent());
			driver.switchTo().alert().accept();
			System.out.println("Handled 'Already logged in' alert popup successfully.");
		} catch (Exception e) {
			System.out.println("No JS alert displayed, moving forward.");
		}

		// Explicit Wait: Wait until page redirects to dashboard/tickets view
		WebDriverWait redirectWait = new WebDriverWait(driver, Duration.ofSeconds(10));
		redirectWait.until(ExpectedConditions.or(ExpectedConditions.urlContains("tickets"),
				ExpectedConditions.urlContains("dashboard")));

		return new TicketPage(driver, action);
	}

	// Negative Login Method (Does not wait for redirection, used for negative test
	// cases)
	public void enterCredentialsAndSignIn(String email, String password) {
		action.enterText(emailInput, email);
		action.enterText(passwordInput, password);
		action.click(signInBtn);
	}

	// Method to capture error toast message for invalid login attempts
	public String getErrorMessage() {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		WebElement errorElement = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageToast));
		return errorElement.getText();
	}

	// Method to wait until any previous error toast disappears from the screen
	public void waitForErrorToastToDisappear() {
		try {
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
			// Wait until the error message element is NOT visible anymore
			wait.until(ExpectedConditions.invisibilityOfElementLocated(errorMessageToast));
		} catch (Exception e) {
			System.out.println("No previous error toast present on screen.");
		}
	}

	// Method to retrieve the JWT Auth Token from Local Storage
	public String getAuthToken() {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			Object token = js.executeScript("return window.localStorage.getItem('omniflow_token');");
			return token != null ? token.toString() : null;
		} catch (Exception e) {
			System.out.println("Error fetching JWT token: " + e.getMessage());
			return null;
		}
	}
}