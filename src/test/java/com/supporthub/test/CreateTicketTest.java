package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.TicketDataProvider;
import com.supporthub.pages.NewTicketModalPage;
import com.supporthub.pages.TicketPage;

public class CreateTicketTest extends BaseClass {

	@Test(priority = 1, dataProvider = "ticketDataProvider", dataProviderClass = com.supporthub.dataprovider.CreateTicketDataProvider.class)
	public void testCreateTicketWithValidCustomer(String customerName, String subject) {

		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Step 1: Click '+' (New Ticket) button on Ticket Page to open the modal
		ticketPage.clickNewTicketButton();

		// Step 2: Fill details dynamically using Data Provider values
		NewTicketModalPage modalPage = new NewTicketModalPage(getDriver(), getActionDriver());
		modalPage.selectCustomerFromDropdown(customerName);
		modalPage.enterSubject(subject);
		modalPage.clickCreateTicket();

		// Step 3: Check mandatory customer validation error
		boolean isErrorPresent = modalPage.isCustomerRequiredErrorDisplayed();
		Assert.assertFalse(isErrorPresent, "Ticket creation failed! 'Customer is required' error toast was displayed.");

		// Step 4: Extract Ticket ID directly from the success toast notification via
		// XPath
		String toastTicketID = modalPage.getCreatedTicketIDFromToast();
		Assert.assertFalse(toastTicketID.isEmpty(),
				"Assertion Failed: Could not extract Ticket ID from 'Ticket created' success toast!");

		// Step 5: Retrieve active ticket ID from the detail header view
		String headerTicketID = ticketPage.getActiveTicketIDFromHeader();
		Assert.assertFalse(headerTicketID.isEmpty(), "Assertion Failed: Header Ticket ID is empty!");

		// Step 6: STRICT CROSS-VERIFICATION (Toast ID vs Header ID)
		Assert.assertEquals(headerTicketID, toastTicketID, "Assertion Failed: Header Ticket ID (" + headerTicketID
				+ ") does not match Toast Ticket ID (" + toastTicketID + ")!");

		// Step 8: Verify customer name dynamically on the ticket panel
		boolean isCustomerMatched = ticketPage.isCustomerNameMatching(customerName);
		Assert.assertTrue(isCustomerMatched, "Assertion Failed: Customer name '" + customerName
				+ "' does not match or is missing on the ticket panel!");
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

		// Verification 1: Verify success toast message ("Interaction logged")
		boolean isToastDisplayed = ticketPage.verifyInteractionLoggedToast();
		Assert.assertTrue(isToastDisplayed, "Assertion Failed: 'Interaction logged' toast message is not displayed!");

		// === NEW VERIFICATION FLOW FOR CLOSED TICKET ===

		// Step 7: Copy/Get the Ticket ID from the active detail header view
		String copiedTicketID = ticketPage.getActiveTicketIDFromHeader();
		Assert.assertFalse(copiedTicketID.isEmpty(),
				"Assertion Failed: Header Ticket ID is empty, cannot verify closed ticket!");
		// Step 8: Click Pin sidebar
		ticketPage.clickPinSidebar();
		// Step 9: Click on the 'Closed' tab from the left sidebar view
		ticketPage.clickClosedTab();

		// Step 10: Search the copied Ticket ID in the search bar
		ticketPage.searchTicketByID(copiedTicketID);

		// Step 10: Verify that the ticket is successfully found under the Closed
		// section with matching ID
		boolean isTicketFound = ticketPage.verifySearchedTicketID(copiedTicketID);
		Assert.assertTrue(isTicketFound,
				"Assertion Failed: Closed ticket with ID '" + copiedTicketID + "' was not found in the Closed list!");

		// Step 11: Verify that the ticket status in the list is strictly 'CLOSED'
		boolean isClosedStatus = ticketPage.isTicketStatusClosedInList();
		Assert.assertTrue(isClosedStatus,
				"Assertion Failed: Ticket status is not 'CLOSED'! It might be Open or Pending.");

	}
}