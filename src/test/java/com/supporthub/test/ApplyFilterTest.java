package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.TicketPage;

public class ApplyFilterTest extends BaseClass {

	@Test(priority = 1)
	public void testApplyQuickFilterAndToggleClose() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String email = prop.getProperty("username");
		String pass = prop.getProperty("password");

		TicketPage ticketPage = loginPage.login(email, pass);

		ticketPage.toggleFilterOverlay();
		ticketPage.selectFilterStatus("Open");
		ticketPage.selectFilterPriority("Urgent");
		ticketPage.toggleFilterOverlay();
	}

	@Test(priority = 2)
	public void testCreateAndSaveCustomFilterViewShortcut() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String email = prop.getProperty("username");
		String pass = prop.getProperty("password");

		TicketPage ticketPage = loginPage.login(email, pass);

		String customViewName = "shortcut open filter";

		// Step 1: Open Advanced Filter overlay
		ticketPage.toggleFilterOverlay();

		// Step 2: Select filter criteria
		ticketPage.selectFilterStatus("Open");
		ticketPage.selectFilterPriority("Urgent");

		// Step 3 & 4: Click 'Save As New', enter view name & save
		ticketPage.saveCustomFilterView(customViewName);

		// Verification: Toast Message Check ("View 'test' saved personally")
		boolean isToastVisible = ticketPage.verifyToastMessageDisplayed(customViewName);
		Assert.assertTrue(isToastVisible, "Assertion Failed: Toast message 'View saved personally' is not displayed!");

		// Step 5: Close overlay
		ticketPage.toggleFilterOverlay();

		// Step 6: Click Pin sidebar
		ticketPage.clickPinSidebar();

		// Step 7 & 8: Click custom view under MY VIEWS
		ticketPage.clickCustomViewShortcut(customViewName);
	}

	@Test(priority = 3)
	public void testSearchWithNonMatchingFilterCriteria() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String email = prop.getProperty("username");
		String pass = prop.getProperty("password");

		TicketPage ticketPage = loginPage.login(email, pass);

		// 1. Click Advanced Filter icon to open overlay
		ticketPage.toggleFilterOverlay();

		// 2, 3 & 4. Click Status, Search non-matching status (e.g., 'Logistics Support
		// required') & press Enter/Checkbox
		ticketPage.selectFilterStatus("Logistics Support required");

		// 5. Click Advanced Filter icon again to close overlay
		ticketPage.toggleFilterOverlay();

		// 6. Verify that "No tickets found" is displayed
		boolean isNoTicketsDisplayed = ticketPage.verifyNoTicketsFoundMessage();
		Assert.assertTrue(isNoTicketsDisplayed, "Assertion Failed: 'No tickets found' message is not displayed!");
	}
	
	@Test(priority = 4)
    public void testSearchTicketById() {
        LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

        String email = prop.getProperty("username"); 
        String pass = prop.getProperty("password");

        TicketPage ticketPage = loginPage.login(email, pass);

        String targetTicketId = "TKT-261009-210882";

        // 1 & 2. Click on search bar and enter valid Ticket ID
        ticketPage.searchTicketByIdOrSubject(targetTicketId);

        // 3. Verify that the correct matching ticket card is filtered and displayed
        boolean isTicketFound = ticketPage.verifyTicketDisplayedInList(targetTicketId);
        Assert.assertTrue(isTicketFound, "Assertion Failed: Ticket " + targetTicketId + " is not displayed in the filtered list!");
    }
}