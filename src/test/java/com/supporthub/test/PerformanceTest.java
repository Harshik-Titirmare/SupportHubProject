package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.PerformanceDataProvider;
import com.supporthub.pages.PerformancePage;

public class PerformanceTest extends BaseClass {

	@Test(priority = 1, dataProvider = "agentSearchData", dataProviderClass = PerformanceDataProvider.class)
	public void testSearchAgentInPerformanceGrid(String searchTerm, String expectedAgentName) {
		// Initialize PerformancePage directly since login is handled by BaseClass
		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 1: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 2: Search existing agent name
		performancePage.searchAgent(searchTerm);

		// Verification: Grid filters and displays only target agent row
		boolean isAgentFound = performancePage.isAgentRowDisplayed(expectedAgentName);
		Assert.assertTrue(isAgentFound, "Assertion Failed: Target agent row was not displayed in the filtered grid!");
	}

	@Test(priority = 2, dataProvider = "invalidAgentSearchData", dataProviderClass = PerformanceDataProvider.class)
	public void testSearchNonExistingAgentInPerformanceGrid(String invalidSearchTerm) {
		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 1: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 2: Enter invalid search string in agent search
		performancePage.searchAgent(invalidSearchTerm);

		// Verification: Check for "No data for selected filter" message
		boolean isNoDataMsgVisible = performancePage.verifyNoDataMessageDisplayed();
		Assert.assertTrue(isNoDataMsgVisible,
				"Assertion Failed: 'No data for selected filter' message is not displayed for invalid search!");
	}

	@Test(priority = 3, dataProvider = "exportExcelData", dataProviderClass = PerformanceDataProvider.class)
	public void testExportExcelReportGeneration(String teamName, String dateFilter) {
		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 1: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 2: Select Team filter
		performancePage.selectTeam(teamName);

		// Step 3: Select Date range filter
		performancePage.selectDateFilter(dateFilter);

		// Step 4: Click 'Export Excel' button
		performancePage.clickExportExcelButton();

		// Step 5: Verify file download using ActionDriver utility method with correct
		// file prefix
		boolean isFileDownloaded = getActionDriver().waitForFileDownload("agent-productivity", 15);

		Assert.assertTrue(isFileDownloaded, "Assertion Failed: Excel report file was not downloaded locally!");
	}
}