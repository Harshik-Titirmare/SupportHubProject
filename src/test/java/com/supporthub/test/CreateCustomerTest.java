package com.supporthub.test;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import com.supporthub.base.BaseClass;
import com.supporthub.pages.CustomerPage;
import com.supporthub.dataprovider.CustomerDataProvider;

public class CreateCustomerTest extends BaseClass {

	// CLEANUP FIX: Refreshes page after EVERY test method to ensure 100% clean DOM
	// state
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

		// === NEW VERIFICATION 3: Search by Phone, Check Single Result & Validate Name
		// ===
		customerPage.searchCustomerByPhone(phone);

		boolean isValidSingleResult = customerPage.verifySingleCustomerAndName(name);
		Assert.assertTrue(isValidSingleResult,
				"Assertion Failed: Either multiple customers appeared, or the searched phone '" + phone
						+ "' does not match expected name '" + name + "'!");

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
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/CustomerFile.csv";
		customerPage.uploadCSVFile(filePath);

		// Step 4: Click Modal Import button
		customerPage.clickModalImportButton();

		// Verification: Check success toast message ("Import complete")
		boolean isImportSuccessful = customerPage.verifyImportSuccessToast();
		Assert.assertTrue(isImportSuccessful, "Assertion Failed: CSV import success toast message is not displayed!");

		// Verification 2: Read ALL customers from CSV and verify each one in a loop
		List<String[]> allCustomers = customerPage.getAllCustomersFromCSV(filePath);

		System.out.println("Total customers found in CSV for verification: " + allCustomers.size());

		for (String[] customer : allCustomers) {
			String csvName = customer[0];
			String csvPhone = customer[1];

			System.out.println("Verifying imported customer -> Name: " + csvName + ", Phone: " + csvPhone);

			// Search by phone and verify unique record match
			customerPage.searchCustomerByPhone(csvPhone);
			boolean isCustomerVerified = customerPage.verifySingleCustomerAndName(csvName);

			Assert.assertTrue(isCustomerVerified, "Assertion Failed: Imported customer '" + csvName + "' with phone '"
					+ csvPhone + "' is missing or not unique!");
		}
	}

	@Test(priority = 4)
	public void testCustomerDeduplicationViaCSV() {
		CustomerPage customerPage = new CustomerPage(getDriver(), getActionDriver());

		// Step 1: Navigate to Customers Tab
		customerPage.navigateToCustomers();

		// Step 2: Click 'Import' button
		customerPage.clickImportButton();

		// Step 3: Upload CSV file containing pre-existing Email/Phone
		String filePath = System.getProperty("user.dir") + "/src/test/resources/testdata/CustomerFile.csv";
		customerPage.uploadCSVFile(filePath);

		// Step 4: Click Modal Import button
		customerPage.clickModalImportButton();

		// Verification: Check update toast message
		boolean isUpdateSuccessful = customerPage.verifyCustomerUpdatedToastMessage();
		Assert.assertTrue(isUpdateSuccessful,
				"Assertion Failed: Customer deduplication/update toast message is not displayed!");

	}
}