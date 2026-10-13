package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.ReminderDataProvider;
import com.supporthub.pages.ReminderPage;

public class ReminderTest extends BaseClass {

	@Test(priority = 1, dataProvider = "reminderData", dataProviderClass = ReminderDataProvider.class)
	public void testSetTicketReminder(String note, String date, String time) {
		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());

		// Step 1: Click on the first ticket
		reminderPage.clickFirstTicket();

		// Step 2: Click on the 'Reminder' button
		reminderPage.clickReminderButton();

		// Step 3: Enter note, future date and time using DataProvider parameters
		reminderPage.fillReminderDetails(note, date, time);

		// Step 4: Click 'Set Reminder'
		reminderPage.clickSubmitSetReminder();

		try {
			Thread.sleep(1000);
			// Verification: Check toast message ("Reminder set")
			boolean isReminderSet = reminderPage.verifyReminderToast();
			Assert.assertTrue(isReminderSet, "Assertion Failed: Reminder success toast message is not displayed!");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Refresh to reset state for the next test case
			getDriver().navigate().refresh();
		}
	}

	@Test(priority = 2, dataProvider = "blankReminderNoteData", dataProviderClass = ReminderDataProvider.class)
	public void testBlankReminderNoteValidation(String note, String date, String time) {
		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());

		// Step 1: Click on the first ticket
		reminderPage.clickFirstTicket();

		// Step 2: Click on the 'Reminder' button
		reminderPage.clickReminderButton();

		// Step 3: Fill reminder details using DataProvider parameters (Note is blank)
		reminderDataFill(reminderPage, note, date, time);

		// Verification: 'Set Reminder' button should be disabled when note is blank
		boolean isBtnEnabled = reminderPage.isSetReminderButtonEnabled();
		Assert.assertFalse(isBtnEnabled,
				"Assertion Failed: 'Set Reminder' button is enabled even when Reminder Note is blank!");
	}

	private void reminderDataFill(ReminderPage page, String note, String date, String time) {
		page.fillReminderDetails(note, date, time);
	}
}