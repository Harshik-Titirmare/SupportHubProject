package com.supporthub.test;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.pages.CustomerPage;
import com.supporthub.dataprovider.CustomerDataProvider;

public class CreateCustomerTest extends BaseClass {

	// CLEANUP FIX: Refreshes page after EVERY test method to ensure 100% clean DOM state
	@AfterMethod(alwaysRun = true)
	public void cleanUpAfterMethod() {
		try {
			getDriver().navigate().refresh();
			staticWait(2);
		} catch (Exception ignored) {
		}
	}

	@Test(priority = 1, dataProvider = "addCustomerData", dataProviderClass = CustomerDataProvider.class)
	public void testCreateNewCustomer(String name, String phone, String email, String country, String state,
			String source, String dob, String gender, String address, String notes, String city, String pincode,
			String socialMedia) {

		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 1: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 2: Click '+ New' Button
		customerPage.clickNewCustomerButton();

		// Step 3: Fill form details using DataProvider parameters
		customerPage.fillCustomerDetails(name, phone, email, country, state, source, dob, gender, address, notes, city,
				pincode, socialMedia);

		// Step 4: Click 'Create Customer'
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

		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		customerPage.navigateToCustomers();
		customerPage.clickNewCustomerButton();

		// Fill details with blank Name as per DataProvider
		customerPage.fillCustomerDetails(name, phone, email, country, state, source, dob, gender, address, notes, city,
				pincode, socialMedia);

		// Verification: When Name is blank, "Create Customer" button should be disabled
		boolean isBtnEnabledWhenBlank = customerPage.isCreateCustomerButtonEnabled();
		Assert.assertFalse(isBtnEnabledWhenBlank,
				"Assertion Failed: 'Create Customer' button is enabled even when Name field is blank!");
	}

	@Test(priority = 3)
	public void testImportCustomersCSV() {
		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 1: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 2: Click 'Import' button
		customerPage.clickImportButton();

		// Step 3: Upload CSV file
		String filePath = "/home/altius/Documents/CustomerFile.csv";
		customerPage.uploadCSVFile(filePath);

		// Step 4: Click Modal Import button
		customerPage.clickModalImportButton();

		// Verification: Check success toast message ("Import complete")
		boolean isImportSuccessful = customerPage.verifyImportSuccessToast();
		Assert.assertTrue(isImportSuccessful, "Assertion Failed: CSV import success toast message is not displayed!");
	}

	@Test(priority = 4)
	public void testCustomerDeduplicationViaCSV() {
		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 1: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 2: Click 'Import' button
		customerPage.clickImportButton();

		// Step 3: Upload CSV file containing pre-existing Email/Phone
		String filePath = "/home/altius/Documents/CustomerFile.csv";
		customerPage.uploadCSVFile(filePath);

		// Step 4: Click Modal Import button
		customerPage.clickModalImportButton();

		// Verification: Check update toast message
		boolean isUpdateSuccessful = customerPage.verifyCustomerUpdatedToastMessage();
		Assert.assertTrue(isUpdateSuccessful,
				"Assertion Failed: Customer deduplication/update toast message is not displayed!");
	}
}