package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.ReminderDataProvider;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.ReminderPage;

public class ReminderTest extends BaseClass {

	@Test(priority = 1)
	public void testSetTicketReminder() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());

		// Step 2: Click on the first ticket[cite: 11]
		reminderPage.clickFirstTicket();

		// Step 3: Click on the 'Reminder' button[cite: 11, 12]
		reminderPage.clickReminderButton();

		// Step 4: Enter note, future date and time[cite: 13]
		reminderPage.fillReminderDetails("Follow up on payment callback", "10-18-2026", "07:50PM");

		// Step 5: Click 'Set Reminder'
		reminderPage.clickSubmitSetReminder();

		// Verification: Check toast message ("Reminder set")
		boolean isReminderSet = reminderPage.verifyReminderToast();
		Assert.assertTrue(isReminderSet, "Assertion Failed: Reminder success toast message is not displayed!");
	}

	@Test(priority = 2, dataProvider = "blankReminderNoteData", dataProviderClass = ReminderDataProvider.class)
	public void testBlankReminderNoteValidation(String note, String date, String time) {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());

		// Step 2: Click on the first ticket
		reminderPage.clickFirstTicket();

		// Step 3: Click on the 'Reminder' button
		reminderPage.clickReminderButton();

		// Step 4: Fill reminder details using DataProvider parameters (Note is blank)
		reminderPage.fillReminderDetails(note, date, time);

		// Verification: 'Set Reminder' button should be disabled when note is blank
		boolean isBtnEnabled = reminderPage.isSetReminderButtonEnabled();
		Assert.assertFalse(isBtnEnabled,
				"Assertion Failed: 'Set Reminder' button is enabled even when Reminder Note is blank!");
	}
}