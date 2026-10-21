package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class CreateTicketDataProvider {

    @DataProvider(name = "ticketDataProvider")
    public Object[][] getTicketData() {
        return new Object[][] {
            { "Falguni", "Test Issue for Late Delivery" },
            { "Rahul Sharma", "Payment Gateway Failure Issue" },
            { "Priya Verma", "Login OTP Not Received" }
        };
    }
}