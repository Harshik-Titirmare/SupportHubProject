package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class ApplyFilterDataProvider {

	@DataProvider(name = "quickFilterData")
	public static Object[][] getQuickFilterData() {
		return new Object[][] { { "Open", "Urgent" } };
	}

	@DataProvider(name = "customViewFilterData")
	public static Object[][] getCustomViewFilterData() {
		return new Object[][] { { "shortcut open filter", "Open", "High" } };
	}

	@DataProvider(name = "nonMatchingFilterData")
	public static Object[][] getNonMatchingFilterData() {
		return new Object[][] { { "Logistics Support required" } };
	}

	@DataProvider(name = "searchTicketData")
	public static Object[][] getSearchTicketData() {
		return new Object[][] { { "TKT-261018-274737" } };
	}

	@DataProvider(name = "globalViewFilterData")
	public static Object[][] getGlobalViewFilterData() {
		return new Object[][] { { "High Priority", "Open", "High" } };
	}
}