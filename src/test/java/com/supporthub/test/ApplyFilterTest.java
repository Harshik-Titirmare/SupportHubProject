package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.ApplyFilterDataProvider;
import com.supporthub.pages.TicketPage;

public class ApplyFilterTest extends BaseClass {

	@Test(priority = 1, dataProvider = "quickFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testApplyQuickFilterAndToggleClose(String status, String priority) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		ticketPage.toggleFilterOverlay();
		ticketPage.selectFilterStatus(status);
		ticketPage.selectFilterPriority(priority);
		ticketPage.toggleFilterOverlay();
	}

	@Test(priority = 2, dataProvider = "customViewFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testCreateAndSaveCustomFilterViewShortcut(String customViewName, String status, String priority) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Step 1: Open Advanced Filter overlay
		ticketPage.toggleFilterOverlay();

		// Step 2: Select filter criteria using DataProvider parameters
		ticketPage.selectFilterStatus(status);
		ticketPage.selectFilterPriority(priority);

		// Step 3 & 4: Click 'Save As New', enter view name & save
		ticketPage.saveCustomFilterView(customViewName);

		// Verification: Toast Message Check
		boolean isToastVisible = ticketPage.verifyToastMessageDisplayed(customViewName);
		Assert.assertTrue(isToastVisible, "Assertion Failed: Toast message for saved view is not displayed!");

		// Step 5: Close overlay
		ticketPage.toggleFilterOverlay();

		// Step 6: Click Pin sidebar
		ticketPage.clickPinSidebar();

		// Step 7 & 8: Click custom view under MY VIEWS
		ticketPage.clickCustomViewShortcut(customViewName);
	}

	@Test(priority = 3, dataProvider = "nonMatchingFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testSearchWithNonMatchingFilterCriteria(String nonMatchingStatus) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// 1. Click Advanced Filter icon to open overlay
		ticketPage.toggleFilterOverlay();

		// 2, 3 & 4. Select non-matching status criteria from DataProvider
		ticketPage.selectFilterStatus(nonMatchingStatus);

		// 5. Click Advanced Filter icon again to close overlay
		ticketPage.toggleFilterOverlay();

		// 6. Verify that "No tickets found" is displayed
		boolean isNoTicketsDisplayed = ticketPage.verifyNoTicketsFoundMessage();
		Assert.assertTrue(isNoTicketsDisplayed, "Assertion Failed: 'No tickets found' message is not displayed!");
	}

	@Test(priority = 4, dataProvider = "searchTicketData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testSearchTicketById(String targetTicketId) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// 1 & 2. Click on search bar and enter valid Ticket ID from DataProvider
		ticketPage.searchTicketByIdOrSubject(targetTicketId);

		// 3. Verify that the correct matching ticket card is filtered and displayed
		boolean isTicketFound = ticketPage.verifyTicketDisplayedInList(targetTicketId);
		Assert.assertTrue(isTicketFound,
				"Assertion Failed: Ticket " + targetTicketId + " is not displayed in the filtered list!");
	}

	@Test(priority = 5, dataProvider = "globalViewFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testCreateAndSaveGlobalFilterViewShortcut(String globalViewName, String status, String priority) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		// Step 1: Open Advanced Filter overlay
		ticketPage.toggleFilterOverlay();

		// Step 2: Select Status and Priority from DataProvider
		ticketPage.selectFilterStatus(status);
		ticketPage.selectFilterPriority(priority);

		// Step 3, 4 & 5: Scroll down, click 'Save As New', enter view name, check 'Save
		// as Global View' and save
		ticketPage.saveGlobalFilterView(globalViewName);

		// Verification: Toast Message Check
		boolean isToastVisible = ticketPage.verifyGlobalViewToastMessage(globalViewName);
		Assert.assertTrue(isToastVisible, "Assertion Failed: Toast message for saved global view is not displayed!");
	}
}