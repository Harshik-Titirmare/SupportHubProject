package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.pages.LoginPage;
import com.supporthub.pages.CustomerPage;
import com.supporthub.dataprovider.CustomerDataProvider;

public class CreateCustomerTest extends BaseClass {

	@Test(priority = 1, dataProvider = "addCustomerData", dataProviderClass = CustomerDataProvider.class)
	public void testCreateNewCustomer(String name, String phone, String email, String country, String state,
			String source, String dob, String gender, String address, String notes, String city, String pincode,
			String socialMedia) {

		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 2: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 3: Click '+ New' Button
		customerPage.clickNewCustomerButton();

		// Step 4: Fill form details using DataProvider parameters (13 parameters
		// matching CustomerPage)
		customerPage.fillCustomerDetails(name, phone, email, country, state, source, dob, gender, address, notes, city,
				pincode, socialMedia);

		// Step 5: Click 'Create Customer'
		customerPage.clickCreateCustomerButton();

		// Verification 1: Toast Message Check ("Customer created")
		boolean isToastVisible = customerPage.verifyCustomerCreatedToastMessage();
		Assert.assertTrue(isToastVisible, "Assertion Failed: 'Customer created' toast message is not displayed!");

		// Verification 2: Customer Profile Check in List
		boolean isCustomerVisible = customerPage.verifyCustomerCreated(name);
		Assert.assertTrue(isCustomerVisible,
				"Assertion Failed: New customer record for " + name + " is not visible in the profile list!");
	}

	@Test(priority = 2, dataProvider = "blankNameCustomerData", dataProviderClass = CustomerDataProvider.class)
	public void testBlankMandatoryNameValidation(String name, String phone, String email, String country, String state,
			String source, String dob, String gender, String address, String notes, String city, String pincode,
			String socialMedia) {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		loginPage.login(appUser, appPass);

		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		customerPage.navigateToCustomers();
		customerPage.clickNewCustomerButton();

		// 1. Fill details with blank Name
		customerPage.fillCustomerDetails(name, phone, email, country, state, source, dob, gender, address, notes, city,
				pincode, socialMedia);

		// Verification 1: When Name is blank, "Create Customer" button should be
		// disabled
		boolean isBtnEnabledWhenBlank = customerPage.isCreateCustomerButtonEnabled();
		Assert.assertFalse(isBtnEnabledWhenBlank,
				"Assertion Failed: 'Create Customer' button is enabled even when Name field is blank!");

	}

	@Test(priority = 3)
	public void testImportCustomersCSV() {
		LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

		String appUser = prop.getProperty("username");
		String appPass = prop.getProperty("password");

		// Step 1: Login
		loginPage.login(appUser, appPass);

		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 2: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 3: Click 'Import' button
		customerPage.clickImportButton();

		// Step 4: Upload CSV file from /home/altius/Documents/CustomerFile.csv
		String filePath = "/home/altius/Documents/CustomerFile.csv";
		customerPage.uploadCSVFile(filePath);

		// Step 5: Click Modal Import button
		customerPage.clickModalImportButton();

		// Verification: Check success toast message ("Import complete")
		boolean isImportSuccessful = customerPage.verifyImportSuccessToast();
		Assert.assertTrue(isImportSuccessful, "Assertion Failed: CSV import success toast message is not displayed!");
	}
	
	@Test(priority = 4)
    public void testCustomerDeduplicationViaCSV() {
        LoginPage loginPage = new LoginPage(getDriver(), getActionDriver());

        String appUser = prop.getProperty("username"); 
        String appPass = prop.getProperty("password");

        // Step 1: Login
        loginPage.login(appUser, appPass);

        CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

        // Step 2: Navigate to Customers Tab
        customerPage.navigateToCustomers();

        // Step 3: Click 'Import' button
        customerPage.clickImportButton();

        // Step 4: Upload CSV file containing pre-existing Email/Phone
        String filePath = "/home/altius/Documents/CustomerFile.csv";
        customerPage.uploadCSVFile(filePath);

        // Step 5: Click Modal Import button
        customerPage.clickModalImportButton();
        
        
        // Verification: Check update toast message ("0 created, 1 updated")
        boolean isUpdateSuccessful = customerPage.verifyCustomerUpdatedToastMessage();
        Assert.assertTrue(isUpdateSuccessful, "Assertion Failed: Customer deduplication/update toast message is not displayed!");
    }
	
	
}