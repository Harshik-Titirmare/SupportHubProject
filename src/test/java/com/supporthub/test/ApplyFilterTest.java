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

		// Step 1: Open filter overlay, apply Status & Priority, and close overlay
		ticketPage.toggleFilterOverlay();
		ticketPage.selectFilterStatus(status);
		ticketPage.selectFilterPriority(priority);
		ticketPage.toggleFilterOverlay();

		// Step 2: Click on the first filtered ticket in the list
		ticketPage.clickFirstTicketInList();

		// Step 3: Fetch Status and Priority from the right-side detail header view
		String actualHeaderStatus = ticketPage.getActiveTicketStatusFromHeader();
		String actualHeaderPriority = ticketPage.getActiveTicketPriorityFromHeader();

		// Step 4: Strict Assertions matching with Data Provider parameters (ignoring
		// case for safety)
		Assert.assertTrue(actualHeaderStatus.equalsIgnoreCase(status),
				"Assertion Failed: Ticket Status mismatch! Expected from DataProvider: '" + status
						+ "', but found on header: '" + actualHeaderStatus + "'");

		Assert.assertTrue(actualHeaderPriority.equalsIgnoreCase(priority),
				"Assertion Failed: Ticket Priority mismatch! Expected from DataProvider: '" + priority
						+ "', but found on header: '" + actualHeaderPriority + "'");
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

		// Step 9: Click on the first filtered ticket in the list after applying custom
		// view
		ticketPage.clickFirstTicketInList();

		// Step 10: Fetch Status and Priority from the right-side detail header view
		String actualHeaderStatus = ticketPage.getActiveTicketStatusFromHeader();
		String actualHeaderPriority = ticketPage.getActiveTicketPriorityFromHeader();

		// Step 11: Strict Assertions matching with DataProvider parameters (ignoring
		// case for safety)
		Assert.assertTrue(actualHeaderStatus.equalsIgnoreCase(status),
				"Assertion Failed: Ticket Status mismatch! Expected from DataProvider: '" + status
						+ "', but found on header: '" + actualHeaderStatus + "'");

		Assert.assertTrue(actualHeaderPriority.equalsIgnoreCase(priority),
				"Assertion Failed: Ticket Priority mismatch! Expected from DataProvider: '" + priority
						+ "', but found on header: '" + actualHeaderPriority + "'");
	}

	@Test(priority = 3, dataProvider = "nonMatchingFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testSearchWithNonMatchingFilterCriteria(String nonMatchingStatus) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());

		try {
			// 1. Click Advanced Filter icon to open overlay
			ticketPage.toggleFilterOverlay();

			// 2, 3 & 4. Select non-matching status criteria from DataProvider
			ticketPage.selectFilterStatus(nonMatchingStatus);

			// 5. Click Advanced Filter icon again to close overlay
			ticketPage.toggleFilterOverlay();

			// 6. Verify that "No tickets found" is displayed
			boolean isNoTicketsDisplayed = ticketPage.verifyNoTicketsFoundMessage();
			Assert.assertTrue(isNoTicketsDisplayed, "Assertion Failed: 'No tickets found' message is not displayed!");

		} finally {
			// Cleanup: Reset the filter so subsequent tests start with a clean filter state
			ticketPage.resetFilters();
		}
	}

	@Test(priority = 4, dataProvider = "searchTicketData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testSearchTicketById(String targetTicketId) {
		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());
		boolean isPassed = false;
		try {
			// 1 & 2. Click on search bar and enter valid Ticket ID from DataProvider
			ticketPage.searchTicketByIdOrSubject(targetTicketId);

			// 3. Verify that the correct matching ticket card is filtered and displayed
			boolean isTicketFound = ticketPage.verifyTicketDisplayedInList(targetTicketId);
			Assert.assertTrue(isTicketFound,
					"Assertion Failed: Ticket " + targetTicketId + " is not displayed in the filtered list!");
			staticWait(2);
		} finally {
			// Cleanup: Clear the search bar so subsequent tests start with a clean state
			if (isPassed) {
				ticketPage.clearSearchBox();
			}

		}
	}

	@Test(priority = 5, dataProvider = "globalViewFilterData", dataProviderClass = ApplyFilterDataProvider.class)
	public void testCreateAndSaveGlobalFilterViewShortcut(String globalViewName, String status, String priority) {

		TicketPage ticketPage = new TicketPage(getDriver(), getActionDriver());
		ticketPage.clearSearchBox();
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

		// Step 5: Close overlay
		ticketPage.toggleFilterOverlay();

		// Step 6: Click Pin sidebar
//		ticketPage.clickPinSidebar();

		// Step 7 & 8: Click global view under GLOBAL VIEWS
		// (Note: Use clickGlobalViewShortcut if available, or clickCustomViewShortcut
		// depending on your page implementation)
		ticketPage.clickGlobalViewShortcut(globalViewName);

		// Step 9: Click on the first filtered ticket in the list after applying global
		// view
		ticketPage.clickFirstTicketInList();

		// Step 10: Fetch Status and Priority from the right-side detail header view
		String actualHeaderStatus = ticketPage.getActiveTicketStatusFromHeader();
		String actualHeaderPriority = ticketPage.getActiveTicketPriorityFromHeader();

		// Step 11: Strict Assertions matching with DataProvider parameters (ignoring
		// case for safety)
		Assert.assertTrue(actualHeaderStatus.equalsIgnoreCase(status),
				"Assertion Failed: Ticket Status mismatch! Expected from DataProvider: '" + status
						+ "', but found on header: '" + actualHeaderStatus + "'");

		Assert.assertTrue(actualHeaderPriority.equalsIgnoreCase(priority),
				"Assertion Failed: Ticket Priority mismatch! Expected from DataProvider: '" + priority
						+ "', but found on header: '" + actualHeaderPriority + "'");
	}
}