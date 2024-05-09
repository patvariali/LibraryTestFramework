package com.library.pages;

import com.library.utilities.Driver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    public LoginPage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(id = "inputEmail")
    private WebElement emailInput;

    @FindBy(id = "inputPassword")
    private WebElement passwordInput;

    @FindBy(css = "button[type=submit]")
    private WebElement singInBtn;

    public MainPage doLogin(String email, String password) {

        emailInput.sendKeys(email);
        passwordInput.sendKeys(password);
        singInBtn.click();

        return new MainPage();
    }
}
