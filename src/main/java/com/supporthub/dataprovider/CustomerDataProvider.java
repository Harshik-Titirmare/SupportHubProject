package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class CustomerDataProvider {

    @DataProvider(name = "addCustomerData")
    public static Object[][] getCustomerData() {
        return new Object[][] {
            // Positive Test Data (TC_CUST_001)
            { 
                "Rahul Sharma", "9876543210", "rahul.sharma@example.com", 
                "India", "Maharashtra", "Social Media", "12/15/1995", 
                "Male", "MG Road", "VIP Customer", "Nagpur", "440001", "@rahul_sharma" 
            }
        };
    }

    // Negative Test Data Provider for Blank Name Validation (TC_CUST_002)
    @DataProvider(name = "blankNameCustomerData")
    public static Object[][] getBlankNameData() {
        return new Object[][] {
            { 
                "", "9876543210", "blank.name@example.com", 
                "India", "Maharashtra", "Social Media", "12/15/1995", 
                "Male", "MG Road", "Test Notes", "Nagpur", "440001", "@test" 
            }
        };
    }
}