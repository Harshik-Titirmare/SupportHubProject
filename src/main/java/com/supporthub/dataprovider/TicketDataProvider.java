package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class TicketDataProvider {
	
	@DataProvider(name = "ticketCategoryInteractionData")
    public static Object[][] getTicketCategoryData() {
        return new Object[][] {
            { "Order Issues", "Order Details", "order details incorrect", "Closed", "Customer confirmed issue is resolved via callback." }
        };
    }
}
