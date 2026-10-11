package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.NewTicketModalPage;
import com.supporthub.pages.TicketPage;

public class CreateTicketTest extends BaseClass {

    @Test(priority = 1)
    public void testCreateTicketWithValidCustomer() {
        // 1. Initialize Login Page
        LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

        // 2. Fetch Credentials from config.properties
        String email = prop.getProperty("username"); 
        String pass = prop.getProperty("password");

        // 3. Perform Login -> Returns TicketPage instance
        TicketPage ticketPage = loginPage.login(email, pass);

        // 4. Click '+' (New Ticket) button on Ticket Page to open Modal
        ticketPage.clickNewTicketButton();

        // 5. Fill and submit details inside the New Ticket Modal
        NewTicketModalPage modalPage = new NewTicketModalPage(getDriver(), getActionDriver());
        modalPage.selectCustomerFromDropdown("Falguni");
        modalPage.enterSubject("Test Issue for Late Delivery");
        modalPage.clickCreateTicket();

        // 6. Assertions
        boolean isErrorPresent = modalPage.isCustomerRequiredErrorDisplayed();
        Assert.assertFalse(isErrorPresent, "Ticket creation failed! 'Customer is required' error toast was displayed.");

        boolean isSuccessToastDisplayed = modalPage.isTicketCreatedToastDisplayed();
        Assert.assertTrue(isSuccessToastDisplayed, "Ticket creation confirmation failed! 'Ticket created' toast was not displayed.");
    }

    @Test(priority = 2)
    public void testCreateTicketValidationOnMissingFields() {
        // TC_TKT_002: Negative Test Case for Mandatory Fields Validation
        LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

        String email = prop.getProperty("username"); 
        String pass = prop.getProperty("password");

        TicketPage ticketPage = loginPage.login(email, pass);
        ticketPage.clickNewTicketButton();

        NewTicketModalPage modalPage = new NewTicketModalPage(getDriver(), getActionDriver());
        
        // 1. Enter customer without selecting dropdown option OR leave subject blank
        modalPage.enterCustomerWithoutSelecting("Vishali");

        // 2. Click 'Create Ticket' button
        modalPage.clickCreateTicket();

        // 3. Assertion: Verify validation toast error appears ('Subject is required' or 'Customer is required')
        boolean isSubjectErrorDisplayed = modalPage.isSubjectRequiredErrorDisplayed();
        boolean isCustomerErrorDisplayed = modalPage.isCustomerRequiredErrorDisplayed();

        Assert.assertTrue(isSubjectErrorDisplayed || isCustomerErrorDisplayed, 
                "Validation error failed! Neither 'Subject is required' nor 'Customer is required' toast error was displayed.");
    }
}