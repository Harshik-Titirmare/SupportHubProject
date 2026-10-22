package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.JSONDataReader;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.TicketPage;

public class LoginTest extends BaseClass {

	@Test(priority = 1, description = "TC_AUTH_019 - Workspace Isolation Check - Valid Demo Credentials on UAT Workspace")
	public void testWorkspaceIsolation() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		// Step 2 & 3: Fetch UAT workspace and demo credentials from JSON
		String workspace = JSONDataReader.getData("uatWorkspace", "workspace");
		String email = JSONDataReader.getData("uatWorkspace", "email");
		String password = JSONDataReader.getData("uatWorkspace", "validPassword");

		loginPage.enterWorkspaceAndContinue(workspace);

		// Step 4: Verify heading displays 'Sign into uat'
		String actualHeading = loginPage.getSignInHeadingText();
		Assert.assertTrue(actualHeading.contains("Sign in " + workspace),
				"Assertion Failed: UAT workspace heading verification failed! Actual heading: " + actualHeading);
		// Step 5 & 6: Enter demo credentials on UAT workspace and click Sign In
		loginPage.enterCredentialsAndSignIn(email, password);

		// Step 7: Verify that login fails and error message toast appears
		String errorMsg = loginPage.getErrorMessage();
		Assert.assertTrue(errorMsg.contains("Login failed") || errorMsg.contains("Invalid credentials"),
				"Assertion Failed: Expected error message was not displayed for cross-workspace login!");

		System.out.println("Test case TC_AUTH_019 (Workspace Isolation Check) passed successfully!");
	}

	@Test(priority = 2, description = "TC_AUTH_021 - Login with Invalid Password on Demo Workspace")
	public void testLoginWithInvalidPassword() {

		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		// Step 0: Ensure any residual toast from the previous test is gone
		loginPage.waitForErrorToastToDisappear();

		// Step 1 & 2: Fetch data from JSON and navigate to 'demo' workspace
		String workspace = JSONDataReader.getData("demoWorkspace", "workspace");
		String email = JSONDataReader.getData("demoWorkspace", "email");
		String invalidPassword = JSONDataReader.getData("demoWorkspace", "invalidPassword");

		loginPage.enterWorkspaceAndContinue(workspace);
		staticWait(5);
		// Step 3 & 4: Enter valid email and incorrect password, then click Sign In
		loginPage.enterCredentialsAndSignIn(email, invalidPassword);

		// Step 5: Verify that login fails and error message toast appears
		String errorMsg = loginPage.getErrorMessage();
		Assert.assertTrue(errorMsg.contains("Login failed") || errorMsg.contains("Invalid credentials"),
				"Assertion Failed: Expected error message for invalid password was not displayed!");

		System.out.println("Test case TC_AUTH_021 (Invalid Password Check) passed successfully!");
	}

	@Test(priority = 3, description = "TC_AUTH_018 - Valid Standard Email & Password Login in Demo Workspace")
	public void testValidUserLogin() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		// Step 0: Ensure any residual toast from the previous test is gone
		loginPage.waitForErrorToastToDisappear();

		// Step 1 & 3: Read workspace name, enter it, clear pre-filled text, and click
		// continue
		String workspaceName = prop.getProperty("workspace");
		loginPage.enterWorkspaceAndContinue(workspaceName);

		// Step 4: Verify heading on the credentials screen shows 'Sign in demo'
		String actualHeading = loginPage.getSignInHeadingText();
		Assert.assertTrue(actualHeading.contains("Sign in " + workspaceName),
				"Assertion Failed: Workspace heading verification failed! Actual heading: " + actualHeading);
		System.out.println("Workspace heading verified successfully: " + actualHeading);

		// Step 5 & 6: Read credentials and perform login
		String email = prop.getProperty("username");
		String password = prop.getProperty("password");
		TicketPage ticketPage = loginPage.login(email, password);

		// Step 7: Verify successful URL redirection and JWT Token storage
		String currentUrl = getDriver().getCurrentUrl();
		Assert.assertTrue(currentUrl.contains("tickets") || currentUrl.contains("dashboard"),
				"Assertion Failed: Login failed or user was not redirected to the main dashboard/tickets view!");

		String token = loginPage.getAuthToken();
		Assert.assertNotNull(token, "Assertion Failed: JWT Auth Token ('omniflow_token') is null!");
		Assert.assertFalse(token.trim().isEmpty(), "Assertion Failed: JWT Auth Token ('omniflow_token') is empty!");

		System.out.println("Login test case TC_AUTH_018 executed successfully! JWT Token verified.");
	}
}