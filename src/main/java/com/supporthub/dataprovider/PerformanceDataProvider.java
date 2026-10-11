package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class PerformanceDataProvider {

	@DataProvider(name = "agentSearchData")
	public static Object[][] getAgentSearchData() {
		return new Object[][] { { "Harshik", "Harshik Titirmare" } };
	}

	@DataProvider(name = "invalidAgentSearchData")
	public static Object[][] getInvalidAgentSearchData() {
		return new Object[][] { { "XYZTest" } };
	}

	@DataProvider(name = "exportExcelData")
	public static Object[][] getExportExcelData() {
		return new Object[][] { { "Chat support", "This Month" } };
	}
}