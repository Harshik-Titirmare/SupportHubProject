package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.ReminderDataProvider;
import com.supporthub.pages.ReminderPage;
import com.supporthub.pages.TicketPage;

public class ReminderTest extends BaseClass {

	@Test(priority = 1, dataProvider = "reminderData", dataProviderClass = ReminderDataProvider.class)
	public void testSetTicketReminder(String note, String date, String time) {
		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Step 1: Click on the first ticket and get Ticket ID from TicketPage header
		// method
		reminderPage.clickFirstTicket();
		String capturedTicketId = ticketPage.getActiveTicketIDFromHeader();
		System.out.println("Captured Ticket ID: " + capturedTicketId);

		// Step 2: Click on the 'Reminder' button
		reminderPage.clickReminderButton();

		// Step 3: Enter note, future date and time using DataProvider parameters
		reminderPage.fillReminderDetails(note, date, time);

		// Step 4: Click 'Set Reminder'
		reminderPage.clickSubmitSetReminder();

		try {
			Thread.sleep(1000);
			// Verification 1: Check toast message ("Reminder set")
			boolean isReminderSet = reminderPage.verifyReminderToast();
			Assert.assertTrue(isReminderSet, "Assertion Failed: Reminder success toast message is not displayed!");

			// Verification 2: Navigate to Reminders tab from left sidebar and validate
			// Ticket ID & Note
			reminderPage.navigateToRemindersPage();
			Thread.sleep(1000);
			boolean isReminderVerifiedInList = reminderPage.verifyReminderInList(capturedTicketId, note);
			Assert.assertTrue(isReminderVerifiedInList, "Assertion Failed: Reminder with Ticket ID '" + capturedTicketId
					+ "' and Note '" + note + "' not found in reminders list!");

		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Test failed due to exception: " + e.getMessage());
		} finally {
			// Refresh and navigate back to Tickets page so next test starts fresh from
			// Tickets view
			getDriver().navigate().refresh();
			TicketPage ticketPageCleanup = new TicketPage(getDriver(), getActionDriver());
			ticketPageCleanup.navigateToTickets(); // Make sure this method exists in TicketPage to go back to Tickets
													// tab
		}
	}

	@Test(priority = 2, dataProvider = "blankReminderNoteData", dataProviderClass = ReminderDataProvider.class)
	public void testBlankReminderNoteValidation(String note, String date, String time) {
		ReminderPage reminderPage = new ReminderPage(getDriver(), getActionDriver());
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Ensure we are on the Tickets page before starting test 2
		ticketPage.navigateToTickets();

		// Step 1: Click on the first ticket
		reminderPage.clickFirstTicket();

		// Step 2: Click on the 'Reminder' button
		reminderPage.clickReminderButton();

		// Step 3: Fill reminder details using DataProvider parameters (Note is blank)
		reminderPage.reminderDataFill(reminderPage, note, date, time);

		// Verification: 'Set Reminder' button should be disabled when note is blank
		boolean isBtnEnabled = reminderPage.isSetReminderButtonEnabled();
		Assert.assertFalse(isBtnEnabled,
				"Assertion Failed: 'Set Reminder' button is enabled even when Reminder Note is blank!");
	}
}