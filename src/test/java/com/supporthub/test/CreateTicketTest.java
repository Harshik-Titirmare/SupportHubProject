package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.TicketDataProvider;
import com.supporthub.pages.NewTicketModalPage;
import com.supporthub.pages.TicketPage;

public class CreateTicketTest extends BaseClass {

	@Test(priority = 1)
	public void testCreateTicketWithValidCustomer() {

		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// 1. Click '+' (New Ticket) button on Ticket Page to open Modal
		ticketPage.clickNewTicketButton();

		// 2. Fill and submit details inside the New Ticket Modal
		NewTicketModalPage modalPage = new NewTicketModalPage(getDriver(), getActionDriver());
		modalPage.selectCustomerFromDropdown("Falguni");
		modalPage.enterSubject("Test Issue for Late Delivery");
		modalPage.clickCreateTicket();

		// 3. Assertions
		boolean isErrorPresent = modalPage.isCustomerRequiredErrorDisplayed();
		Assert.assertFalse(isErrorPresent, "Ticket creation failed! 'Customer is required' error toast was displayed.");

		boolean isSuccessToastDisplayed = modalPage.isTicketCreatedToastDisplayed();
		Assert.assertTrue(isSuccessToastDisplayed,
				"Ticket creation confirmation failed! 'Ticket created' toast was not displayed.");
	}

	@Test(priority = 2)
	public void testCreateTicketValidationOnMissingFields() {

		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		ticketPage.clickNewTicketButton();

		NewTicketModalPage modalPage = new NewTicketModalPage(getDriver(), getActionDriver());

		// 1. Enter customer without selecting dropdown option OR leave subject blank
		modalPage.enterCustomerWithoutSelecting("Vishali");

		// 2. Click 'Create Ticket' button
		modalPage.clickCreateTicket();

		// 3. Assertion: Verify validation toast error appears
		boolean isSubjectErrorDisplayed = modalPage.isSubjectRequiredErrorDisplayed();
		boolean isCustomerErrorDisplayed = modalPage.isCustomerRequiredErrorDisplayed();

		Assert.assertTrue(isSubjectErrorDisplayed || isCustomerErrorDisplayed,
				"Validation error failed! Neither 'Subject is required' nor 'Customer is required' toast error was displayed.");

		// 4. FIX: Close/Cancel the modal so that the page returns to clean state for
		// Test 3
		modalPage.clickCancelButton(); // Ensure NewTicketModalPage has clickCancelButton() method, or use ESC key /
										// close icon
	}

	@Test(priority = 3, dataProvider = "ticketCategoryInteractionData", dataProviderClass = TicketDataProvider.class)
	public void testTicketCategorySelectionAndInteractionLogging(String category, String subcategory,
			String subSubcategory, String targetStatus, String interactionNote) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Step 1: Click on the first ticket in the list
		ticketPage.clickFirstTicketInList();

		// Step 2, 3 & 4: Set Category, Subcategory and Sub-subcategory
		ticketPage.setTicketCategoryFlow(category, subcategory, subSubcategory);

		// Step 5: Click status (Open) and change it to Closed
		ticketPage.changeTicketStatusToClosed(targetStatus);

		// Step 6: Fill manual interaction note and click 'Log Interaction'
		ticketPage.fillInteractionNotesAndLog(interactionNote);

		// Verification: Verify success toast message ("Interaction logged")
		boolean isToastDisplayed = ticketPage.verifyInteractionLoggedToast();
		Assert.assertTrue(isToastDisplayed, "Assertion Failed: 'Interaction logged' toast message is not displayed!");
	}
}