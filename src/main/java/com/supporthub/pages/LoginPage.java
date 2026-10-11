package com.supporthub.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.supporthub.actiondriver.ActionDriver;

public class LoginPage {

    private WebDriver driver;
    private ActionDriver action;

    private By emailInput = By.xpath("//input[@type='email' or contains(@placeholder,'company.com')]");
    private By passwordInput = By.xpath("//input[@type='password']");
    private By signInBtn = By.xpath("//button[contains(text(),'Sign In')]");

    public LoginPage(WebDriver driver, ActionDriver action) {
        this.driver = driver;
        this.action = action;
    }

    public TicketPage login(String email, String password) {
        action.enterText(emailInput, email);
        action.enterText(passwordInput, password);
        action.click(signInBtn);

        // Safe Alert Handling
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            wait.until(ExpectedConditions.alertIsPresent());
            driver.switchTo().alert().accept();
            System.out.println("Handled 'Already logged in' alert popup successfully.");
        } catch (Exception e) {
            System.out.println("No JS alert displayed, moving forward.");
        }

        return new TicketPage(driver, action);
    }
}