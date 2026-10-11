package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.dataprovider.PerformanceDataProvider;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.PerformancePage;

public class PerformanceTest extends BaseClass {

	@Test(priority = 1, dataProvider = "agentSearchData", dataProviderClass = PerformanceDataProvider.class)
	public void testSearchAgentInPerformanceGrid(String searchTerm, String expectedAgentName) {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 2: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 3: Search existing agent name
		performancePage.searchAgent(searchTerm);

		// Verification: Grid filters and displays only target agent row
		boolean isAgentFound = performancePage.isAgentRowDisplayed(expectedAgentName);
		Assert.assertTrue(isAgentFound, "Assertion Failed: Target agent row was not displayed in the filtered grid!");
	}

	@Test(priority = 2, dataProvider = "invalidAgentSearchData", dataProviderClass = PerformanceDataProvider.class)
	public void testSearchNonExistingAgentInPerformanceGrid(String invalidSearchTerm) {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 2: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 3: Enter invalid string 'XYZTest' in agent search
		performancePage.searchAgent(invalidSearchTerm);

		// Verification: Check for "No data for selected filter" message
		boolean isNoDataMsgVisible = performancePage.verifyNoDataMessageDisplayed();
		Assert.assertTrue(isNoDataMsgVisible,
				"Assertion Failed: 'No data for selected filter' message is not displayed for invalid search!");
	}

	@Test(priority = 3, dataProvider = "exportExcelData", dataProviderClass = PerformanceDataProvider.class)
	public void testExportExcelReportGeneration(String teamName, String dateFilter) {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		PerformancePage performancePage = new PerformancePage(getDriver(), getActionDriver());

		// Step 2: Open Performance Tab
		performancePage.navigateToPerformance();

		// Step 3: Select Team filter ('Chat support')
		performancePage.selectTeam(teamName);

		// Step 4: Select Date range filter ('This Month')
		performancePage.selectDateFilter(dateFilter);

		// Step 5: Click 'Export Excel' button
		performancePage.clickExportExcelButton();

		// Step 6: Verify file download using ActionDriver utility method with correct
		// file prefix
		boolean isFileDownloaded = getActionDriver().waitForFileDownload("agent-productivity", 15);

		Assert.assertTrue(isFileDownloaded, "Assertion Failed: Excel report file was not downloaded locally!");
	}
}