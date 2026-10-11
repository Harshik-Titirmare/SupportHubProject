package com.supporthub.dataprovider;

import org.testng.annotations.DataProvider;

public class ReminderDataProvider {

    @DataProvider(name = "reminderData")
    public static Object[][] getReminderData() {
        return new Object[][] {
            // Positive Test Data (TC_REM_001)
            { "Follow up on payment callback", "10-18-2026", "07:50PM" }
        };
    }

    @DataProvider(name = "blankReminderNoteData")
    public static Object[][] getBlankReminderData() {
        return new Object[][] {
            // Negative Test Data (TC_REM_002 - Blank Note)
            { "", "10-18-2026", "07:50PM" }
        };
    }
}